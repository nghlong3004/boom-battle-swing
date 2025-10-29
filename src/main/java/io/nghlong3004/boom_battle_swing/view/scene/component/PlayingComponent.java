package io.nghlong3004.boom_battle_swing.view.scene.component;

import io.nghlong3004.boom_battle_swing.model.Bomber;
import io.nghlong3004.boom_battle_swing.view.GameApplication;
import io.nghlong3004.boom_battle_swing.view.scene.AbstractScene;
import io.nghlong3004.boom_battle_swing.view.scene.GameScene;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static io.nghlong3004.boom_battle_swing.constant.GameConstant.SCALE;
import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.IMAGE_BOMBER_HEIGHT;
import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.IMAGE_BOMBER_WIDTH;

public class PlayingComponent extends AbstractScene implements GameScene {

    @Getter
    private Bomber bomber;
    private final PauseComponent pauseScene;
    @Setter
    private boolean paused = true;

    public PlayingComponent(GameApplication game) {
        super(game);
        initClasses();
        pauseScene = new PauseComponent(this);
    }

    private void initClasses() {
        bomber = new Bomber(0f, 0f, (int) (IMAGE_BOMBER_WIDTH * SCALE), (int) (IMAGE_BOMBER_HEIGHT * SCALE));
    }

    @Override
    public void update() {
        if (paused) {
            pauseScene.update();
        }
        else {
            bomber.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        bomber.render(g);
        if (paused) {
            pauseScene.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused) {
            pauseScene.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused) {
            pauseScene.mouseReleased(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (paused) {
            pauseScene.mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused) {
            pauseScene.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> bomber.setLeft(true);
            case KeyEvent.VK_D -> bomber.setRight(true);
            case KeyEvent.VK_W -> bomber.setUp(true);
            case KeyEvent.VK_S -> bomber.setDown(true);
            case KeyEvent.VK_ENTER -> paused = !paused;
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
