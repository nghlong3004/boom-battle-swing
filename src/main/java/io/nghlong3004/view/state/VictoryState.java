package io.nghlong3004.view.state;

import io.nghlong3004.model.State;
import io.nghlong3004.util.ObjectContainer;
import io.nghlong3004.view.component.VictoryComponent;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static io.nghlong3004.constant.AudioConstant.WIN;

@Slf4j
public class VictoryState implements GameState {

    private final StateContext stateContext;
    private final VictoryComponent victoryComponent;

    protected VictoryState(StateContext stateContext) {
        this.stateContext = stateContext;
        this.victoryComponent = new VictoryComponent(stateContext);
    }

    @Override
    public void update() {
        victoryComponent.update();
    }

    @Override
    public void render(Graphics g) {
        stateContext.getPlayingState().getGameSystem().setSilentMode(true);
        stateContext.getPlayingState().render(g);
        stateContext.getPlayingState().getGameSystem().setSilentMode(false);

        victoryComponent.render(g);
    }

    @Override
    public void exit() {
        log.info("Exiting VictoryState");
    }

    @Override
    public void enter() {
        log.info("Entering VictoryState");
        victoryComponent.resetAnimation();

        ObjectContainer.getAudioUtil().playEffect(WIN);
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
        victoryComponent.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        victoryComponent.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        victoryComponent.mouseMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
}
