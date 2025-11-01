package io.nghlong3004.view;


import io.nghlong3004.controller.KeyboardController;
import io.nghlong3004.controller.MouseController;
import io.nghlong3004.controller.MouseMotionController;
import io.nghlong3004.view.state.StateContext;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

import static io.nghlong3004.constant.GameConstant.GAME_HEIGHT;
import static io.nghlong3004.constant.GameConstant.GAME_WIDTH;

public class GamePanel extends JPanel {

    @Getter
    private final StateContext context;

    public GamePanel(StateContext context) {
        this.context = context;
        setInput();
        setSize();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        context.render(g);
    }

    private void setInput() {
        addKeyListener(new KeyboardController(this));
        addMouseListener(new MouseController(this));
        addMouseMotionListener(new MouseMotionController(this));
    }

    private void setSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }
}
