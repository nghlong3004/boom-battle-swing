package io.nghlong3004.boom_battle_swing.view.scene.component;

import io.nghlong3004.boom_battle_swing.constant.ImageConstant;
import io.nghlong3004.boom_battle_swing.model.Bomber;
import io.nghlong3004.boom_battle_swing.view.GameApplication;
import io.nghlong3004.boom_battle_swing.view.map.LevelManager;
import io.nghlong3004.boom_battle_swing.view.render.BomberRenderer;
import io.nghlong3004.boom_battle_swing.view.render.Renderer;
import io.nghlong3004.boom_battle_swing.view.scene.AbstractScene;
import io.nghlong3004.boom_battle_swing.view.scene.KeyboardScene;
import io.nghlong3004.boom_battle_swing.view.scene.Scene;
import io.nghlong3004.boom_battle_swing.view.update.BomberUpdater;
import io.nghlong3004.boom_battle_swing.view.update.Updater;
import lombok.Getter;

import java.awt.*;
import java.awt.event.KeyEvent;

import static io.nghlong3004.boom_battle_swing.constant.AudioConstant.MOVE;
import static io.nghlong3004.boom_battle_swing.constant.GameConstant.SCALE;
import static io.nghlong3004.boom_battle_swing.util.ObjectContainer.getAudioPlayer;

public class PlayingComponent extends AbstractScene implements Scene, KeyboardScene {

    @Getter
    private Bomber bomber;
    private Updater updater;
    private Renderer renderer;
    private LevelManager levelManager;
    private static final long MOVE_SFX_COOLDOWN_MS = 120;
    private long lastMoveSfxAtMs = 0L;

    public PlayingComponent(GameApplication game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        bomber = new Bomber(200f, 200f, (int) (50 * SCALE), (int) (30 * SCALE), ImageConstant.IKE);
        bomber.setLevelData(levelManager.getLevelData());
        updater = new BomberUpdater();
        renderer = new BomberRenderer();
    }

    public void reset() {
        bomber.resetAll();
    }

    @Override
    public void update() {
        updater.update(bomber);
    }

    @Override
    public void draw(Graphics g) {
        levelManager.draw(g);
        renderer.render(g, bomber);
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
