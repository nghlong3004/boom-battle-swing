package io.nghlong3004.game;

import io.nghlong3004.context.GameContext;
import io.nghlong3004.input.KeyboardInput;
import io.nghlong3004.input.MouseInput;
import io.nghlong3004.input.MouseMotionInput;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

import static io.nghlong3004.constant.GameConstant.GAME_HEIGHT;
import static io.nghlong3004.constant.GameConstant.GAME_WIDTH;

public class GamePanel extends JPanel {
    @Getter
    private final GameContext gameContext;

    public GamePanel(GameContext gameContext) {
        this.gameContext = gameContext;
        setInput();
        setSize();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameContext.render(g);
    }

    public void update() {
        gameContext.update();
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
