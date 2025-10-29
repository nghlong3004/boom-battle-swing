package io.nghlong3004.boom_battle_swing.view;

import io.nghlong3004.boom_battle_swing.view.scene.GameScene;
import io.nghlong3004.boom_battle_swing.view.scene.GameState;
import io.nghlong3004.boom_battle_swing.view.scene.MenuScene;
import io.nghlong3004.boom_battle_swing.view.scene.PlayingScene;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

import static io.nghlong3004.boom_battle_swing.constant.FrameRateConstant.FPS_SET;
import static io.nghlong3004.boom_battle_swing.constant.FrameRateConstant.UPS_SET;

@Slf4j
@Getter
public class GameApplication implements Runnable {
    private final GameScene menu;
    private final PlayingScene playing;
    private final GameWindow gameWindow;
    private final GamePanel gamePanel;
    private Thread gameThread;

    public GameApplication() {
        menu = new MenuScene(this);
        playing = new PlayingScene(this);
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        log.debug("starting game..");
        gameThread.start();
    }

    public void update() {
        switch (GameState.state) {
            case MENU -> menu.update();
            case PLAYING -> playing.update();
            case OPTION -> {
            }
            case QUIT -> System.exit(0);
        }
    }

    public void render(Graphics graphics) {
        switch (GameState.state) {
            case MENU -> menu.render(graphics);
            case PLAYING -> playing.render(graphics);
        }
    }

    @Override
    public void run() {
        log.debug("Running game...");
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;
        long lastCheck = System.currentTimeMillis();
        long previousTime = System.nanoTime();
        int frames = 0;
        int updates = 0;
        double deltaU = 0;
        double deltaF = 0;
        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                ++updates;
                --deltaU;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                ++frames;
                --deltaF;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                log.debug("FPS: {} | UPS: {}", frames, updates);
                frames = 0;
                updates = 0;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

    }

    public void windowFocusLost() {
        if (GameState.state == GameState.PLAYING) {
            playing.getBomber().resetDirection();
        }
    }
}
