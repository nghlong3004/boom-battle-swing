package io.nghlong3004.view.state;

import io.nghlong3004.model.State;
import io.nghlong3004.view.component.GameOverComponent;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

@Slf4j
public class GameOverState implements GameState {

    private final StateContext stateContext;
    private final GameOverComponent gameOverComponent;

    protected GameOverState(StateContext stateContext) {
        this.stateContext = stateContext;
        this.gameOverComponent = new GameOverComponent(stateContext);
    }

    @Override
    public void update() {
        gameOverComponent.update();
    }

    @Override
    public void render(Graphics g) {
        stateContext.getPlayingState().render(g);
        gameOverComponent.render(g);
    }

    @Override
    public void exit() {
        log.info("Exiting GameOverState");
    }

    @Override
    public void enter() {
        log.info("Entering GameOverState");
        gameOverComponent.resetAnimation();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            stateContext.changeState(State.MENU);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gameOverComponent.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        gameOverComponent.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        gameOverComponent.mouseMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
}
