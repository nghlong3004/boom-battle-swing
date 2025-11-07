package io.nghlong3004.manager;

import io.nghlong3004.ai.algorithm.AStartPathFinder;
import io.nghlong3004.ai.algorithm.PathFinder;
import io.nghlong3004.util.MapCollisionChecker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ManagerFactory {

    public static GameManager createGameManager() {
        log.debug("Init MapManager");
        MapManager mapManager = new MapManager();
        log.debug("Init PathFinder");
        PathFinder pathFinder = new AStartPathFinder(mapManager);
        log.debug("Init BomberManager");
        BomberManager bomberManager = new BomberManager();
        log.debug("Init AgentManager");
        AgentManager agentManager = new AgentManager(mapManager, pathFinder, bomberManager);
        log.debug("Init BombManager");
        BombManager bombManager = new BombManager();
        log.debug("Init ItemManager");
        ItemManager itemManager = new ItemManager();
        log.debug("Init MapCollisionChecker");
        MapCollisionChecker collisionChecker = new MapCollisionChecker(mapManager);
        collisionChecker.setBomberManager(bomberManager);
        collisionChecker.setBombManager(bombManager);
        bomberManager.setBombManager(bombManager);
        bomberManager.setCollisionChecker(collisionChecker);
        log.debug("Init ExplosionManager");
        ExplosionManager explosionManager = new ExplosionManager(mapManager, bomberManager, agentManager, itemManager);
        bombManager.setExplosionManager(explosionManager);
        log.debug("Init GameRender");
        GameRender gameRender = new GameRender(mapManager, bomberManager, bombManager, explosionManager, agentManager);
        return GameManager.builder()
                          .bomberManager(bomberManager)
                          .mapManager(mapManager)
                          .itemManager(itemManager)
                          .explosionManager(explosionManager)
                          .gameRender(gameRender)
                          .bombManager(bombManager)
                          .agentManager(agentManager)
                          .build();
    }

}
