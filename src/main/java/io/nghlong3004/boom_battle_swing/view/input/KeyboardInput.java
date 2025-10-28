package io.nghlong3004.boom_battle_swing.view.input;

import io.nghlong3004.boom_battle_swing.view.GamePanel;
import io.nghlong3004.boom_battle_swing.view.scene.GameState;
import lombok.AllArgsConstructor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@AllArgsConstructor
public class KeyboardInput implements KeyListener {

    private final GamePanel gamePanel;

    @Override
    public void keyPressed(KeyEvent e) {
        switch (GameState.state) {
            case MENU -> gamePanel.getGame().getMenu().keyPressed(e);
            case PLAYING -> gamePanel.getGame().getPlaying().keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (GameState.state) {
            case MENU -> gamePanel.getGame().getMenu().keyReleased(e);
            case PLAYING -> gamePanel.getGame().getPlaying().keyReleased(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
