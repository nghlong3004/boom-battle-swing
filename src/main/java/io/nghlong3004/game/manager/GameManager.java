package io.nghlong3004.game.manager;

import io.nghlong3004.model.entities.Bomber;
import io.nghlong3004.model.type.ItemType;
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

    private void play(List<Bomber> bombers) {
        setSpawnBombers(bombers);
        this.bombers = bombers;
        bomberManager.addAll(bombers);
        gameTimer.reset();
        gameTimer.start();
    }

    public void playOffline(MapType type, List<Bomber> bombers) {
        mapManager.loadMap(type);
        itemManager.setItemTypes(getItemTypes());
        setSpawnAgents(bombers.size());
        play(bombers);
    }

    public void playOnline(NetworkManager networkManager) {
        mapManager.getMap()
                  .setType(networkManager.getCurrentRoom()
                                         .getMap());
        mapManager.getMap()
                  .setData(networkManager.getMapData());
        mapManager.setBomberSpawns(networkManager.getSpawns()
                                                 .stream()
                                                 .map(pointResponse -> new Point(pointResponse.x(), pointResponse.y()))
                                                 .toList());
        itemManager.setItemTypes(networkManager.getItemSpawns());
        play(networkManager.getBombers());
    }

    public void reset() {
        if (agents != null) {
            agentManager.stop();
            agents = null;
        }
        bomberManager.reset();
        bombManager.reset();
        explosionManager.reset();
        itemManager.reset();
        gameTimer.reset();
        bombers = null;
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

    public boolean isBomberLocalAlive(String bomberId) {
        if (bombers == null) {
            return true;
        }
        return bombers.stream()
                      .anyMatch(bomber -> bomber.getBomberId()
                                                .equals(bomberId) && bomber.isAlive());
    }

    public boolean isAnyAgentAlive() {
        if (agents == null || agents.isEmpty()) {
            return false;
        }
        return agents.stream()
                     .anyMatch(Bomber::isAlive);
    }

    public boolean isBomberNotLocalAlive(String bomberId) {
        if (bombers == null) {
            return true;
        }
        return bombers.stream()
                      .allMatch(Bomber::isAlive);
    }

    private ItemType[][] getItemTypes() {
        int n = mapManager.getMap()
                          .getData().length;
        int m = mapManager.getMap()
                          .getData()[0].length;
        var itemSpawns = new ItemType[n][m];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                itemSpawns[i][j] = ItemType.BLANK;
                if (Math.random() < 0.9) {
                    ItemType randomType = ItemType.random();
                    itemSpawns[i][j] = randomType;
                }
            }
        }
        return itemSpawns;
    }

    private void setSpawnBombers(List<Bomber> bombers) {
        var points = mapManager.getSpawns();
        for (int i = 0; i < bombers.size(); ++i) {
            bombers.get(i)
                   .setX(points.get(i).x);
            bombers.get(i)
                   .setY(points.get(i).y);
            bombers.get(i)
                   .reset();
        }
    }

    private void setSpawnAgents(int start) {
        var points = mapManager.getSpawns();
        agents = new ArrayList<>();
        for (int i = start; i < points.size(); ++i) {
            Bomber agent = new Bomber(points.get(i).x, points.get(i).y, SkinType.BOZ);
            agent.reset();
            agents.add(agent);
        }
        agentManager.setAgents(agents);
        agentManager.setTickMillis(7);
        agentManager.start();
    }
}
