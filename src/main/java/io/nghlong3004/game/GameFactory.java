package io.nghlong3004.game;

import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.main.GameFrame;
import io.nghlong3004.game.main.GameLoop;
import io.nghlong3004.game.main.GamePanel;
import io.nghlong3004.game.main.GameWindow;

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
