package io.nghlong3004.view.component;

import io.nghlong3004.view.GameObject;

import java.awt.event.MouseEvent;

public interface MouseComponent extends GameObject {
    void mouseMoved(MouseEvent e);

    void mouseDragged(MouseEvent e);

    void mouseReleased(MouseEvent e);

    void mousePressed(MouseEvent e);
}
