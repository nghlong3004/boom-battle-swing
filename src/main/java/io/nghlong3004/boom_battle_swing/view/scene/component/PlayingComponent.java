package io.nghlong3004.boom_battle_swing.view.scene.component;

import io.nghlong3004.boom_battle_swing.model.Bomber;
import io.nghlong3004.boom_battle_swing.model.EnemyManager;
import io.nghlong3004.boom_battle_swing.view.GameApplication;
import io.nghlong3004.boom_battle_swing.view.scene.AbstractScene;
import io.nghlong3004.boom_battle_swing.view.scene.KeyboardScene;
import io.nghlong3004.boom_battle_swing.view.scene.Scene;
import lombok.Getter;

import java.awt.*;
import java.awt.event.KeyEvent;

import static io.nghlong3004.boom_battle_swing.constant.AudioConstant.MOVE;
import static io.nghlong3004.boom_battle_swing.constant.GameConstant.SCALE;
import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.IMAGE_BOMBER_HEIGHT;
import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.IMAGE_BOMBER_WIDTH;
import static io.nghlong3004.boom_battle_swing.util.ObjectContainer.getAudioPlayer;

public class PlayingComponent extends AbstractScene implements Scene, KeyboardScene {

    @Getter
    private Bomber bomber;
    private EnemyManager enemyManager;
    private static final long MOVE_SFX_COOLDOWN_MS = 120;
    private long lastMoveSfxAtMs = 0L;

    public PlayingComponent(GameApplication game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        bomber = new Bomber(0f, 0f, (int) (IMAGE_BOMBER_WIDTH * SCALE), (int) (IMAGE_BOMBER_HEIGHT * SCALE));
        enemyManager = new EnemyManager(this);
    }

    public void reset() {
        bomber.resetAll();
    }

    @Override
    public void update() {
        enemyManager.update();
        bomber.update();
    }

    @Override
    public void draw(Graphics g) {
        bomber.render(g);
        enemyManager.draw(g, 0);
        if (bomber.isMoving()) {
            long now = System.currentTimeMillis();
            if (now - lastMoveSfxAtMs >= MOVE_SFX_COOLDOWN_MS) {
                getAudioPlayer().playEffect(MOVE);
                lastMoveSfxAtMs = now;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> bomber.setLeft(true);
            case KeyEvent.VK_D -> bomber.setRight(true);
            case KeyEvent.VK_W -> bomber.setUp(true);
            case KeyEvent.VK_S -> bomber.setDown(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> bomber.setLeft(false);
            case KeyEvent.VK_D -> bomber.setRight(false);
            case KeyEvent.VK_W -> bomber.setUp(false);
            case KeyEvent.VK_S -> bomber.setDown(false);
        }
    }
}
