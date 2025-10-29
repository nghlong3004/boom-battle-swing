package io.nghlong3004.boom_battle_swing.view.scene;

import io.nghlong3004.boom_battle_swing.model.Bomber;
import io.nghlong3004.boom_battle_swing.view.GameApplication;
import lombok.Getter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static io.nghlong3004.boom_battle_swing.constant.GameConstant.SCALE;
import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.IMAGE_BOMBER_HEIGHT;
import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.IMAGE_BOMBER_WIDTH;

public class PlayingScene extends AbstractScene implements GameScene {

    @Getter
    private Bomber bomber;

    public PlayingScene(GameApplication game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        bomber = new Bomber(0f, 0f, (int) (IMAGE_BOMBER_WIDTH * SCALE), (int) (IMAGE_BOMBER_HEIGHT * SCALE));
    }

    @Override
    public void update() {
        bomber.update();
    }

    @Override
    public void render(Graphics g) {
        bomber.render(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                bomber.setLeft(true);
                break;
            case KeyEvent.VK_D:
                bomber.setRight(true);
                break;
            case KeyEvent.VK_W:
                bomber.setUp(true);
                break;
            case KeyEvent.VK_S:
                bomber.setDown(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                bomber.setLeft(false);
                break;
            case KeyEvent.VK_D:
                bomber.setRight(false);
                break;
            case KeyEvent.VK_W:
                bomber.setUp(false);
                break;
            case KeyEvent.VK_S:
                bomber.setDown(false);
                break;
        }
    }
}
