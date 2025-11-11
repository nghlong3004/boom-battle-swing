package io.nghlong3004.game.manager;

import io.nghlong3004.ai.MoveState;
import io.nghlong3004.ai.algorithm.Distance;
import io.nghlong3004.ai.algorithm.Node;
import io.nghlong3004.ai.algorithm.PathFinder;
import io.nghlong3004.constant.GameConstant;
import io.nghlong3004.model.entities.Bomber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.nghlong3004.constant.GameConstant.TILE_SIZE;

@Slf4j
@RequiredArgsConstructor
public class AgentManager extends BomberManager {

    @Getter
    @Setter
    private List<Bomber> agents;
    private final MapManager mapManager;
    private final PathFinder pathFinder;
    private final BomberManager bomberManager;

    private ScheduledExecutorService scheduledExecutorService;
    private final AtomicBoolean running = new AtomicBoolean(false);

    private volatile int tickMillis = 30;

    private final ConcurrentMap<Bomber, MoveState> moveStates = new ConcurrentHashMap<>();

    @Setter
    private ItemManager itemManager;
    @Setter
    private BombManager bombManager;
    @Setter
    private ExplosionManager explosionManager;

    public void start() {
        if (getCollisionChecker() == null) {
            setCollisionChecker(bomberManager.getCollisionChecker());
        }
        if (running.compareAndSet(false, true)) {
            if (scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
                scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            }
            scheduledExecutorService.scheduleAtFixedRate(this::tick, 0, tickMillis, TimeUnit.MILLISECONDS);
            log.info("AgentManager started with tick={}ms", tickMillis);
        }
    }

    public void pause() {
        running.set(!running.get());
    }

    public void stop() {
        if (running.compareAndSet(true, false)) {
            if (scheduledExecutorService != null) {
                scheduledExecutorService.shutdown();
            }
            log.info("AgentManager stopped");
        }
    }

    private void tick() {
        try {
            final List<Bomber> localAgents = this.agents;
            if (!running.get() || localAgents == null || localAgents.isEmpty()) {
                return;
            }

            final int[][] map = mapManager.getMap()
                                          .getData();
            final int spaceY = (GameConstant.MAX_SCREEN_COLUMN - map[0].length) >>> 1;
            final int spaceX = (GameConstant.MAX_SCREEN_ROW - map.length) >>> 1;

            List<Bomber> agentsSnapshot = new java.util.ArrayList<>(localAgents);

            for (Bomber agent : agentsSnapshot) {
                if (agent.isDying()) {
                    agent.updateDeathAnimation();
                    continue;
                }

                if (!agent.isAlive()) {
                    continue;
                }

                MoveState st = moveStates.computeIfAbsent(agent, bomber -> new MoveState());
                if (!st.isMoving()) {
                    chooseDirectionTowardTarget(agent, spaceX, spaceY);
                    st.setMoving(true);
                    st.setDistance(0);
                }

                stepAgentOnce(agent);
                st.setDistance(st.getDistance() + agent.getSpeed());

                if (st.getDistance() >= TILE_SIZE) {
                    st.setMoving(false);
                    st.setDistance(0);
                }
            }

            checkBomberCollisions();

        } catch (Throwable t) {
            log.error("AgentManager tick error", t);
        }
    }

    private void chooseDirectionTowardTarget(Bomber agent, int spaceX, int spaceY) {
        if (!agent.isAlive() || agent.isDying()) {
            return;
        }
        agent.reset();

        final Point start = toGrid(agent, spaceX, spaceY);

        if (tryFleeFromDanger(agent, start, spaceX, spaceY)) {
            return;
        }

        if (tryMoveTowardBomber(agent, start, spaceX, spaceY)) {
            return;
        }

        if (tryMoveTowardItem(agent, start, spaceX, spaceY)) {
            return;
        }

        applyExploreDirection(agent, start, spaceX, spaceY);
    }

    private boolean tryFleeFromDanger(Bomber agent, Point start, int spaceX, int spaceY) {
        if (bombManager == null && explosionManager == null) {
            return false;
        }

        Point nearestDanger = null;
        double minDistance = Double.MAX_VALUE;

        if (bombManager != null && bombManager.getBombs() != null) {
            for (var bomb : bombManager.getBombs()) {
                Point bombPos = new Point(bomb.getGridX() - spaceX, bomb.getGridY() - spaceY);
                double dist = Distance.manhattan(start, bombPos);

                if (dist < 4 && dist < minDistance) {
                    minDistance = dist;
                    nearestDanger = bombPos;
                }
            }
        }

        if (explosionManager != null && explosionManager.getExplosions() != null) {
            for (var explosion : explosionManager.getExplosions()) {
                Point explosionPos = new Point(explosion.getGridX() - spaceX, explosion.getGridY() - spaceY);
                double dist = Distance.manhattan(start, explosionPos);

                if (dist < 3 && dist < minDistance) {
                    minDistance = dist;
                    nearestDanger = explosionPos;
                }
            }
        }

        if (nearestDanger != null) {
            applyFleeDirection(agent, start, nearestDanger);
            return true;
        }

        return false;
    }

    private void applyFleeDirection(Bomber agent, Point agentPos, Point dangerPos) {
        int dx = agentPos.x - dangerPos.x;
        int dy = agentPos.y - dangerPos.y;

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                agent.setDown(true);
            }
            else {
                agent.setUp(true);
            }
        }
        else {
            if (dy > 0) {
                agent.setRight(true);
            }
            else {
                agent.setLeft(true);
            }
        }
    }

    private boolean tryMoveTowardBomber(Bomber agent, Point start, int spaceX, int spaceY) {
        List<Bomber> bombers = bomberManager.getBombers();
        if (bombers == null || bombers.isEmpty()) {
            return false;
        }

        List<Bomber> bombersSnapshot = new java.util.ArrayList<>(bombers);

        for (var target : bombersSnapshot) {
            if (!target.isAlive() || target == agent) {
                continue;
            }

            final Point end = toGrid(target, spaceX, spaceY);
            final List<Node> path = pathFinder.findPath(start, end);
            if (path.isEmpty()) {
                continue;
            }

            applyFirstStepDirection(agent, path.getFirst());
            return true;
        }
        return false;
    }

    private boolean tryMoveTowardItem(Bomber agent, Point start, int spaceX, int spaceY) {
        if (itemManager == null || itemManager.getItems() == null || itemManager.getItems()
                                                                                .isEmpty()) {
            return false;
        }

        Point nearestItemPos = null;
        double minDistance = Double.MAX_VALUE;
        for (var item : itemManager.getItems()) {
            if (item.isCollected()) {
                continue;
            }

            Point itemPos = new Point(item.getGridX() - spaceX, item.getGridY() - spaceY);
            double dist = Distance.manhattan(start, itemPos);

            if (dist < 10 && dist < minDistance) {
                final List<Node> path = pathFinder.findPath(start, itemPos);
                if (!path.isEmpty()) {
                    minDistance = dist;
                    nearestItemPos = itemPos;
                }
            }
        }

        if (nearestItemPos != null) {
            final List<Node> path = pathFinder.findPath(start, nearestItemPos);
            if (!path.isEmpty()) {
                applyFirstStepDirection(agent, path.getFirst());
                return true;
            }
        }

        return false;
    }

    private void applyExploreDirection(Bomber agent, Point start, int spaceX, int spaceY) {
        final int[][] map = mapManager.getMap()
                                      .getData();

        if (ThreadLocalRandom.current()
                             .nextDouble() < 0.7) {
            int centerX = map.length / 2;
            int centerY = map[0].length / 2;
            Point center = new Point(centerX, centerY);

            double distToCenter = Distance.manhattan(start, center);
            if (distToCenter < 3) {
                applyWeightedRandomDirection(agent);
            }
            else {
                final List<Node> path = pathFinder.findPath(start, center);
                if (!path.isEmpty()) {
                    applyFirstStepDirection(agent, path.getFirst());
                }
                else {
                    applyWeightedRandomDirection(agent);
                }
            }
        }
        else {
            applyWeightedRandomDirection(agent);
        }
    }

    private void applyWeightedRandomDirection(Bomber agent) {
        double rand = ThreadLocalRandom.current()
                                       .nextDouble();

        if (rand < 0.25) {
            agent.setLeft(true);
        }
        else if (rand < 0.50) {
            agent.setRight(true);
        }
        else if (rand < 0.75) {
            agent.setUp(true);
        }
        else {
            agent.setDown(true);
        }
    }

    private void applyFirstStepDirection(Bomber agent, Node node) {
        switch (node.getAction()) {
            case 'l' -> agent.setLeft(true);
            case 'r' -> agent.setRight(true);
            case 'u' -> agent.setUp(true);
            case 'd' -> agent.setDown(true);
        }
    }

    private void stepAgentOnce(Bomber agent) {
        updatePosition(agent);
        updateAnimationTick(agent);
        setAnimation(agent);
    }

    private Point toGrid(Bomber bomber, int spaceX, int spaceY) {
        int gridY = (int) (bomber.getBox().x / TILE_SIZE - spaceY);
        int gridX = (int) ((bomber.getBox().y) / TILE_SIZE - spaceX);
        return new Point(gridX, gridY);
    }


    private void checkBomberCollisions() {
        final List<Bomber> localAgents = this.agents;
        if (localAgents == null || localAgents.isEmpty()) {
            return;
        }

        final List<Bomber> players = bomberManager.getBombers();
        if (players == null || players.isEmpty()) {
            return;
        }


        List<Bomber> agentsCopy = new java.util.ArrayList<>(localAgents);
        List<Bomber> playersCopy = new java.util.ArrayList<>(players);

        for (Bomber agent : agentsCopy) {

            if (!agent.isAlive() || agent.isDying()) {
                continue;
            }

            for (Bomber player : playersCopy) {
                if (!player.isAlive() || player == agent) {
                    continue;
                }

                if (agent.getBox()
                         .intersects(player.getBox())) {

                    if (!player.isDying()) {
                        player.startDying();
                        log.info("Agent name: {} killer bomber at position ({}, {})", agent.hashCode(),
                                 player.getBox().x, player.getBox().y);
                    }
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        final List<Bomber> localAgents = this.agents;
        if (localAgents == null) {
            return;
        }

        for (var bomber : localAgents) {

            if (!bomber.isAlive() && !bomber.isDying()) {
                continue;
            }
            renderBomber(g, bomber);
        }
    }

    public void setTickMillis(int ms) {
        this.tickMillis = Math.max(5, ms);
        if (running.get()) {
            stop();
            start();
        }
    }
}
