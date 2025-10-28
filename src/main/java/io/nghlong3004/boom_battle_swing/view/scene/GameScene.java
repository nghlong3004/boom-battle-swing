package io.nghlong3004.boom_battle_swing.view.scene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface GameScene {
    void update();

    void render(Graphics g);

    void mouseClicked(MouseEvent e);

    void mousePressed(MouseEvent e);

    void mouseReleased(MouseEvent e);

    void mouseDragged(MouseEvent e);

    void mouseMoved(MouseEvent e);

    void keyPressed(KeyEvent e);

    void keyReleased(KeyEvent e);
}
