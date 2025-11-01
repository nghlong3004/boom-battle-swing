package io.nghlong3004.controller;

import io.nghlong3004.view.GamePanel;
import lombok.AllArgsConstructor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

@AllArgsConstructor
public class MouseMotionController implements MouseMotionListener {

    private final GamePanel gamePanel;

    @Override
    public void mouseDragged(MouseEvent e) {
        gamePanel.getContext().mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        gamePanel.getContext().mouseMoved(e);
    }
}
