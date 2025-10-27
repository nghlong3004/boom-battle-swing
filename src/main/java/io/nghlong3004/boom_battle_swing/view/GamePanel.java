package io.nghlong3004.boom_battle_swing.view;

import io.nghlong3004.boom_battle_swing.view.input.KeyboardInput;
import io.nghlong3004.boom_battle_swing.view.input.MouseInput;
import io.nghlong3004.boom_battle_swing.view.input.MouseMotionInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GamePanel extends JPanel {
    private final MouseListener mouseInput;
    private final MouseMotionListener mouseMotionListener;

    private float xDelta = 0;
    private float yDelta = 0;

    private float xDir = 1.1f;
    private float yDir = 1.1f;

    public GamePanel() {
        mouseInput = new MouseInput();
        mouseMotionListener = new MouseMotionInput();
        addKeyListener(new KeyboardInput(this));
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseMotionListener);
    }

    public void changeXDelta(int delta) {
        this.xDelta += delta;
    }

    public void changeYDelta(int delta) {
        this.yDelta += delta;
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        updateRectangle();
        graphics.fillRect((int) xDelta, (int) yDelta, 200, 50);
    }

    private void updateRectangle() {
        xDelta += xDir;
        if (xDelta > 400 || xDelta < 0) {
            xDir *= -1;
        }
        yDelta += yDir;
        if (yDelta > 400 || yDelta < 0) {
            yDir *= -1;
        }
    }

}
