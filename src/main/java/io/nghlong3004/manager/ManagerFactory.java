package io.nghlong3004.manager;

import io.nghlong3004.util.CollisionChecker;
import io.nghlong3004.util.MapCollisionChecker;

public class ManagerFactory {

    public static GameManager createGameManager() {
        MapManager mapManager = new MapManager();
        BomberManager bomberManager = new BomberManager();
        BombManager bombManager = new BombManager(mapManager);
        ExplosionManager explosionManager = new ExplosionManager(mapManager);
        
        MapCollisionChecker collisionChecker = new MapCollisionChecker(mapManager);
        collisionChecker.setBomberManager(bomberManager);
        collisionChecker.setBombManager(bombManager);
        
        bomberManager.setBombManager(bombManager);
        bomberManager.setCollisionChecker(collisionChecker);
        bombManager.setExplosionManager(explosionManager);
        explosionManager.setBomberManager(bomberManager);
        
        GameRender gameRender = new GameRender(mapManager, bomberManager, bombManager, explosionManager);
        return new GameManager(mapManager, bomberManager, bombManager, explosionManager, gameRender);
    }

}
