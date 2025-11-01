package io.nghlong3004.view;


import io.nghlong3004.constant.ImageConstant;
import io.nghlong3004.util.ImageLoaderUtil;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

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
        setVisible(true);
        add(panel);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(ImageLoaderUtil.loadImage(ImageConstant.TITLE));
        addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowLostFocus(WindowEvent e) {
                panel.getContext().windowFocusLost();
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
            }
        });
    }
}
