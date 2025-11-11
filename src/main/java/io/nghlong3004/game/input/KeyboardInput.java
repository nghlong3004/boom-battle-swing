package io.nghlong3004.game.input;

import io.nghlong3004.game.main.GamePanel;
import lombok.AllArgsConstructor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@AllArgsConstructor
public class KeyboardInput implements KeyListener {

    private final GamePanel gamePanel;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gamePanel.getGameContext()
                 .keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gamePanel.getGameContext()
                 .keyReleased(e);
    }
}
