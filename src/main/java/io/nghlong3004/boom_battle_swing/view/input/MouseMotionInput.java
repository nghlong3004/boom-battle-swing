package io.nghlong3004.boom_battle_swing.view.input;

import io.nghlong3004.boom_battle_swing.model.GameState;
import io.nghlong3004.boom_battle_swing.view.game.GamePanel;
import lombok.AllArgsConstructor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

@AllArgsConstructor
public class MouseMotionInput implements MouseMotionListener {

    private final GamePanel gamePanel;

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (GameState.state) {
            case MENU -> gamePanel.getGame().getMenu().mouseDragged(e);
            case OPTION -> gamePanel.getGame().getOption().mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (GameState.state) {
            case MENU -> gamePanel.getGame().getMenu().mouseMoved(e);
            case OPTION -> gamePanel.getGame().getOption().mouseMoved(e);
        }
    }
}
