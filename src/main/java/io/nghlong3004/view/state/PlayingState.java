package io.nghlong3004.view.state;

import io.nghlong3004.constant.AudioConstant;
import io.nghlong3004.model.Bomber;
import io.nghlong3004.model.Entity;
import io.nghlong3004.system.GameSystem;
import io.nghlong3004.util.ObjectContainer;
import lombok.Getter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static io.nghlong3004.constant.BomberConstant.BOMBER_HEIGHT;
import static io.nghlong3004.constant.BomberConstant.BOMBER_WIDTH;

public class PlayingState implements GameState {

    @Getter
    private final Entity bomber;
    private final GameSystem gameSystem;
    private final StateContext stateContext;

    protected PlayingState(StateContext stateContext) {
        this.stateContext = stateContext;
        this.bomber = new Bomber(200f, 200f, (int) BOMBER_WIDTH, (int) BOMBER_HEIGHT);
        this.gameSystem = new GameSystem();
        this.gameSystem.add(bomber);
    }

    public void reset() {
        gameSystem.resetAll();
    }

    @Override
    public void update() {
        gameSystem.update();
    }

    @Override
    public void render(Graphics g) {
        gameSystem.render(g);
    }

    @Override
    public void exit() {
        ObjectContainer.getAudioUtil().stopSong();
    }

    @Override
    public void enter() {
        ObjectContainer.getAudioUtil().playSong(AudioConstant.GAME);
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
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

}
