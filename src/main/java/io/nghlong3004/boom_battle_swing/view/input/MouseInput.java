package io.nghlong3004.boom_battle_swing.view.input;

import io.nghlong3004.boom_battle_swing.model.GameState;
import io.nghlong3004.boom_battle_swing.util.ObjectContainer;
import io.nghlong3004.boom_battle_swing.view.game.GamePanel;
import lombok.AllArgsConstructor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static io.nghlong3004.boom_battle_swing.constant.AudioConstant.CLICK;

@AllArgsConstructor
public class MouseInput implements MouseListener {

    private final GamePanel gamePanel;

    @Override
    public void mouseClicked(MouseEvent e) {
        ObjectContainer.getAudioPlayer().playEffect(CLICK);
        switch (GameState.state) {
            case MENU -> gamePanel.getGame().getMenu().mouseClicked(e);
            case OPTION -> gamePanel.getGame().getOption().mouseClicked(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (GameState.state) {
            case MENU -> gamePanel.getGame().getMenu().mousePressed(e);
            case OPTION -> gamePanel.getGame().getOption().mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (GameState.state) {
            case MENU -> gamePanel.getGame().getMenu().mouseReleased(e);
            case OPTION -> gamePanel.getGame().getOption().mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
