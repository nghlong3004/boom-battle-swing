package io.nghlong3004.boom_battle_swing.view;

import io.nghlong3004.boom_battle_swing.view.scene.GameScene;
import io.nghlong3004.boom_battle_swing.view.scene.GameState;
import io.nghlong3004.boom_battle_swing.view.scene.MenuScene;
import io.nghlong3004.boom_battle_swing.view.scene.PlayingScene;
import lombok.Getter;

import java.awt.*;

@Getter
public class Game implements Runnable {
    @Getter
    private final GameScene menu;
    @Getter
    private final GameScene playing;
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 60;
    private final int UPS_SET = 200;

    public Game() {
        menu = new MenuScene(this);
        playing = new PlayingScene(this);
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (GameState.state) {
            case MENU -> menu.update();
            case PLAYING -> playing.update();
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
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }

    }
}
