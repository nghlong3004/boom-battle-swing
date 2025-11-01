package io.nghlong3004.controller;

import io.nghlong3004.view.GamePanel;
import lombok.AllArgsConstructor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@AllArgsConstructor
public class MouseController implements MouseListener {

    private final GamePanel gamePanel;

    @Override
    public void mouseClicked(MouseEvent e) {
        gamePanel.getContext().mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gamePanel.getContext().mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        gamePanel.getContext().mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
