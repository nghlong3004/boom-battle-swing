package io.nghlong3004.view.state;

import io.nghlong3004.view.GameObject;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface GameState extends GameObject {

    void exit();

    void enter();

    void keyPressed(KeyEvent e);

    void keyReleased(KeyEvent e);

    void mouseClicked(MouseEvent e);

    void mousePressed(MouseEvent e);

    void mouseReleased(MouseEvent e);

    void mouseMoved(MouseEvent e);

    void mouseDragged(MouseEvent e);
}
