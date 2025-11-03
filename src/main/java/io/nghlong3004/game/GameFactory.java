package io.nghlong3004.game;

import io.nghlong3004.context.GameContext;

public class GameFactory {

    public static GameWindow createBoomBattle() {
        GameContext gameContext = new GameContext();
        GamePanel gamePanel = new GamePanel(gameContext);
        GameFrame gameFrame = new GameFrame(gamePanel);
        GameLoop gameLoop = new GameLoop(gamePanel);
        Thread thread = new Thread(gameLoop);
        gamePanel.requestFocus();
        GameWindow gameWindow = new GameWindow(gameFrame, thread);
        return gameWindow;
    }

}
