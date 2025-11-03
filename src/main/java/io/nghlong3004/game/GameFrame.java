package io.nghlong3004.game;

import io.nghlong3004.constant.ImageConstant;
import io.nghlong3004.loader.ImageLoader;

import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame(GamePanel panel) {
        super("Boom Battle");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        setting(panel);
    }

    private void setting(GamePanel panel) {
        add(panel);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(ImageLoader.loadImage(ImageConstant.TITLE));

    }

}
