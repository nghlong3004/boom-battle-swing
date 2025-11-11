package io.nghlong3004.game.component.button;

import io.nghlong3004.game.main.GameLogic;
import io.nghlong3004.util.AudioHelper;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class GameButton implements GameLogic {
    @Getter
    @Setter
    protected boolean mousePressed, mouseOver;
    @Getter
    protected int x, y, width, height;
    protected int rowIndex, columnIndex;
    protected Rectangle box;

    private boolean wasMouseOver = false;

    protected GameButton(int x, int y, int width, int height, int rowIndex) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.box = new Rectangle(x, y, width, height);
        this.rowIndex = rowIndex;
    }

    protected GameButton(int x, int y, int width, int height) {
        this(x, y, width, height, 0);
    }


    protected abstract void loadImage();

    public void reset() {
        setMouseOver(false);
        setMousePressed(false);
        wasMouseOver = false;
    }

    public void setMouseOver(boolean mouseOver) {
        if (!wasMouseOver && mouseOver) {
            playHoverSound();
        }
        this.wasMouseOver = this.mouseOver;
        this.mouseOver = mouseOver;
    }

    protected void playHoverSound() {
        AudioHelper.playTouchSound();
    }

    public boolean isMouseOver(MouseEvent e) {
        return box.contains(e.getX(), e.getY());
    }
}
