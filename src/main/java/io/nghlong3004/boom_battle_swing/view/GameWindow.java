package io.nghlong3004.boom_battle_swing.view;

import javax.swing.*;

public class GameWindow {

    private JFrame jFrame;

    public GameWindow(JPanel panel) {
        jFrame = new JFrame();
        jFrame.setSize(400, 400);
        jFrame.setVisible(true);
        jFrame.add(panel);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
