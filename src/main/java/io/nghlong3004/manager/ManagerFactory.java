package io.nghlong3004.manager;

public class ManagerFactory {

    public static GameManager createGameManager() {
        BomberManager bomberManager = new BomberManager();
        MapManager mapManager = new MapManager();
        GameRender gameRender = new GameRender(mapManager, bomberManager);
        return new GameManager(mapManager, bomberManager, gameRender);
    }

}
