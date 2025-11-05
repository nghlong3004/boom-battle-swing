package io.nghlong3004.manager;

public class ManagerFactory {

    public static GameManager createGameManager() {
        MapManager mapManager = new MapManager();
        BomberManager bomberManager = new BomberManager();
        BombManager bombManager = new BombManager(mapManager);
        
        bomberManager.setBombManager(bombManager);
        
        GameRender gameRender = new GameRender(mapManager, bomberManager, bombManager);
        return new GameManager(mapManager, bomberManager, bombManager, gameRender);
    }

}
