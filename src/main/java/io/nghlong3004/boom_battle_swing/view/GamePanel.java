package io.nghlong3004.boom_battle_swing.view;

import io.nghlong3004.boom_battle_swing.view.input.KeyboardInput;
import io.nghlong3004.boom_battle_swing.view.input.MouseInput;
import io.nghlong3004.boom_battle_swing.view.input.MouseMotionInput;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    @Getter
    private final Game game;

    public GamePanel(Game game) {
        this.game = game;
        setInput();
        setSize();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        game.render(graphics);
    }

    private void setInput() {
        addKeyListener(new KeyboardInput(this));
        addMouseListener(new MouseInput(this));
        addMouseMotionListener(new MouseMotionInput(this));
    }

    private void setSize() {
        Dimension size = new Dimension(1280, 800);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

}
