package io.nghlong3004.context.state;

import io.nghlong3004.context.GameContext;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

@RequiredArgsConstructor
public class QuitState implements GameState {

    private final GameContext gameContext;

    @Override
    public void on() {
        System.exit(0);
    }

    @Override
    public void off() {

    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {

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
}
