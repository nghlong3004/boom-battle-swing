package io.nghlong3004.game.input;

import io.nghlong3004.game.main.GamePanel;
import lombok.AllArgsConstructor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

@AllArgsConstructor
public class MouseMotionInput implements MouseMotionListener {
    private final GamePanel gamePanel;

    @Override
    public void mouseDragged(MouseEvent e) {
        gamePanel.getGameContext()
                 .mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        gamePanel.getGameContext()
                 .mouseMoved(e);
    }
}
