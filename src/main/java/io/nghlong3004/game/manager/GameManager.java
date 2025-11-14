package io.nghlong3004.game.manager;

import io.nghlong3004.model.entities.Bomber;
import io.nghlong3004.model.type.MapType;
import io.nghlong3004.model.type.SkinType;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Builder
public class GameManager {
    @Getter
    private final MapManager mapManager;
    @Getter
    private final BomberManager bomberManager;
    @Getter
    private final BombManager bombManager;
    @Getter
    private final ExplosionManager explosionManager;
    @Getter
    private final ItemManager itemManager;
    @Getter
    private final AgentManager agentManager;
    private final GameRender gameRender;
    @Getter
    private final GameTimer gameTimer;
    private List<Bomber> bombers;
    private List<Bomber> agents;

    public void play(MapType type, List<Bomber> bombers, boolean isOnline) {
        mapManager.setType(type);
        mapManager.loadMap();
        setSpawnBombers(bombers, isOnline);
        this.bombers = bombers;
        bomberManager.addAll(bombers);
        gameTimer.reset();
        gameTimer.start();
    }

    private void setSpawnBombers(List<Bomber> bombers, boolean isOnline) {
        var points = mapManager.getSpawns(6);
        for (int i = 0; i < bombers.size(); ++i) {
            bombers.get(i)
                   .setX(points.get(i).x);
            bombers.get(i)
                   .setY(points.get(i).y);
            bombers.get(i)
                   .reset();
        }
        if (!isOnline) {
            agents = new ArrayList<>();
            for (int i = bombers.size(); i < points.size(); ++i) {
                Bomber agent = new Bomber(points.get(i).x, points.get(i).y, SkinType.BOZ);
                agent.reset();
                agents.add(agent);
            }
            agentManager.setAgents(agents);
            agentManager.setTickMillis(7);
            agentManager.start();
        }
    }

    public void reset() {
        agentManager.stop();
        bomberManager.reset();
        bombManager.reset();
        explosionManager.reset();
        itemManager.reset();
        gameTimer.reset();
        bombers = null;
        agents = null;
    }

    public void update() {
        gameTimer.update();
        agentManager.update();
        bomberManager.update();
        bombManager.update();
        explosionManager.update();
        itemManager.update();
        itemManager.checkCollisions(bombers);
        itemManager.checkCollisions(agents);
    }

    public void render(Graphics g) {
        gameRender.render(g);
    }

    public boolean isAnyPlayerAlive() {
        if (bombers == null) {
            return true;
        }
        return bombers.stream()
                      .anyMatch(Bomber::isAlive);
    }

    public boolean isAnyAgentAlive() {
        if (agents == null || agents.isEmpty()) {
            return false;
        }
        return agents.stream()
                     .anyMatch(Bomber::isAlive);
    }
}
