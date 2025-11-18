package io.nghlong3004.game;

import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.GameState;
import io.nghlong3004.game.main.GameFrame;
import io.nghlong3004.game.main.GameLoop;
import io.nghlong3004.game.main.GamePanel;
import io.nghlong3004.game.main.GameWindow;
import io.nghlong3004.game.manager.ManagerFactory;
import io.nghlong3004.game.manager.NetworkManager;
import io.nghlong3004.loader.AudioLoader;
import io.nghlong3004.model.type.GameStateType;

import java.util.EnumMap;

public class Game {
    public static void run() {
        var stateMap = new EnumMap<GameStateType, GameState>(GameStateType.class);
        var audio = new AudioLoader();
        var gameManager = ManagerFactory.createGameManager(audio);
        var networkManager = new NetworkManager();
        var gameContext = new GameContext(stateMap, audio, gameManager, networkManager);
        var gamePanel = new GamePanel(gameContext);
        var gameFrame = new GameFrame(gamePanel);
        var gameLoop = new GameLoop(gamePanel);
        var thread = new Thread(gameLoop);
        gamePanel.requestFocus();
        (new GameWindow(gameFrame, thread)).open();
    }

}
