package io.nghlong3004.boom_battle_swing.view.game;

import io.nghlong3004.boom_battle_swing.view.GameApplication;
import io.nghlong3004.boom_battle_swing.view.input.KeyboardInput;
import io.nghlong3004.boom_battle_swing.view.input.MouseInput;
import io.nghlong3004.boom_battle_swing.view.input.MouseMotionInput;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

import static io.nghlong3004.boom_battle_swing.constant.GameConstant.GAME_HEIGHT;
import static io.nghlong3004.boom_battle_swing.constant.GameConstant.GAME_WIDTH;

public class GamePanel extends JPanel {
    @Getter
    private final GameApplication game;

    public GamePanel(GameApplication game) {
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
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

}
