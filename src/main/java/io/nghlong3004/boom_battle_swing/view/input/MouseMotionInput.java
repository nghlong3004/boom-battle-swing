package io.nghlong3004.boom_battle_swing.view.input;

import io.nghlong3004.boom_battle_swing.view.GamePanel;
import io.nghlong3004.boom_battle_swing.view.scene.GameState;
import lombok.AllArgsConstructor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

@AllArgsConstructor
public class MouseMotionInput implements MouseMotionListener {

    private final GamePanel gamePanel;

    @Override
    public void mouseDragged(MouseEvent e) {
        if (GameState.state == GameState.PLAYING) {
            gamePanel.getGame().getPlaying().mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (GameState.state) {
            case MENU -> gamePanel.getGame().getMenu().mouseMoved(e);
            case PLAYING -> gamePanel.getGame().getPlaying().mouseMoved(e);
        }
    }
}
