package io.nghlong3004.manager;

import io.nghlong3004.ai.MoveState;
import io.nghlong3004.ai.algorithm.Node;
import io.nghlong3004.ai.algorithm.PathFinder;
import io.nghlong3004.constant.GameConstant;
import io.nghlong3004.entity.Bomber;
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

    private ScheduledExecutorService aiExec;
    private final AtomicBoolean running = new AtomicBoolean(false);

    private volatile int tickMillis = 30;

    private final ConcurrentMap<Bomber, MoveState> moveStates = new ConcurrentHashMap<>();

    public void start() {
        if (getCollisionChecker() == null) {
            setCollisionChecker(bomberManager.getCollisionChecker());
        }
        if (running.compareAndSet(false, true)) {
            aiExec = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "AI-Tick");
                t.setDaemon(true);
                return t;
            });
            aiExec.scheduleAtFixedRate(this::tick, 0, tickMillis, TimeUnit.MILLISECONDS);
            log.info("AgentManager started with tick={}ms", tickMillis);
        }
    }

    public void stop() {
        if (running.compareAndSet(true, false)) {
            if (aiExec != null) {
                aiExec.shutdownNow();
                try {
                    aiExec.awaitTermination(1, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread()
                          .interrupt();
                }
                aiExec = null;
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

            for (Bomber agent : localAgents) {
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

        } catch (Throwable t) {
            log.error("AgentManager tick error", t);
        }
    }

    private void chooseDirectionTowardTarget(Bomber agent, int spaceX, int spaceY) {
        if (!agent.isAlive()) {
            return;
        }
        agent.reset();

        final Point start = toGrid(agent, spaceX, spaceY);
        for (var target : bomberManager.getBombers()) {
            if (!target.isAlive() || target == agent) {
                continue;
            }

            final Point end = toGrid(target, spaceX, spaceY);
            final List<Node> path = pathFinder.findPath(start, end);
            if (path.isEmpty()) {
                continue;
            }

            applyFirstStepDirection(agent, path.getFirst());
            break;
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

    @Override
    public void render(Graphics g) {
        final List<Bomber> localAgents = this.agents;
        if (localAgents == null) {
            return;
        }

        for (var bomber : localAgents) {
            if (!bomber.isAlive()) {
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
