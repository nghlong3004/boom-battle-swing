package io.nghlong3004.context.state;

import io.nghlong3004.component.GameComponent;
import io.nghlong3004.component.option.OptionComponent;
import io.nghlong3004.context.GameContext;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class OptionState implements GameState {

    private final GameComponent optionComponent;

    public OptionState(GameContext stateContext, GameComponent audioComponent) {
        this.optionComponent = new OptionComponent(stateContext, audioComponent);
    }


    @Override
    public void update() {
        optionComponent.update();
    }

    @Override
    public void render(Graphics g) {
        optionComponent.render(g);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        optionComponent.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        optionComponent.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        optionComponent.mouseMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        optionComponent.mouseDragged(e);
    }

    @Override
    public void on() {

    }

    @Override
    public void off() {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}
