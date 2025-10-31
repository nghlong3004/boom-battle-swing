package io.nghlong3004.boom_battle_swing.view.scene.component;

import io.nghlong3004.boom_battle_swing.model.Bomber;
import io.nghlong3004.boom_battle_swing.view.GameApplication;
import io.nghlong3004.boom_battle_swing.view.game.GameWorld;
import io.nghlong3004.boom_battle_swing.view.scene.AbstractScene;
import io.nghlong3004.boom_battle_swing.view.scene.KeyboardScene;
import io.nghlong3004.boom_battle_swing.view.scene.Scene;
import lombok.Getter;

import java.awt.*;
import java.awt.event.KeyEvent;

import static io.nghlong3004.boom_battle_swing.constant.BomberConstant.BOMBER_HEIGHT;
import static io.nghlong3004.boom_battle_swing.constant.BomberConstant.BOMBER_WIDTH;

public class PlayingComponent extends AbstractScene implements Scene, KeyboardScene {

    @Getter
    private final Bomber bomber;
    private final GameWorld gameWorld;

    public PlayingComponent(GameApplication game) {
        super(game);
        this.gameWorld = new GameWorld();
        this.bomber = new Bomber(200f, 200f, (int) BOMBER_WIDTH, (int) BOMBER_HEIGHT);
        this.gameWorld.add(bomber);
    }

    public void reset() {
        gameWorld.resetAll();
    }

    @Override
    public void update() {
        gameWorld.update();
    }

    @Override
    public void draw(Graphics g) {
        gameWorld.render(g);
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
