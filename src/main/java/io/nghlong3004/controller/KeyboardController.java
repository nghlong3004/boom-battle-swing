package io.nghlong3004.controller;

import io.nghlong3004.view.GamePanel;
import lombok.AllArgsConstructor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@AllArgsConstructor
public class KeyboardController implements KeyListener {

    private final GamePanel gamePanel;

    @Override
    public void keyPressed(KeyEvent e) {
        gamePanel.getContext().keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gamePanel.getContext().keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
