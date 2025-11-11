package io.nghlong3004.game.input;

import io.nghlong3004.game.main.GamePanel;
import lombok.AllArgsConstructor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

@AllArgsConstructor
public class MouseInput implements MouseListener, MouseWheelListener {

    private final GamePanel gamePanel;

    @Override
    public void mouseClicked(MouseEvent e) {
        gamePanel.getGameContext()
                 .mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gamePanel.getGameContext()
                 .mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        gamePanel.getGameContext()
                 .mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        gamePanel.getGameContext()
                 .mouseWheelMoved(e);
    }
}
