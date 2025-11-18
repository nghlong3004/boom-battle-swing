package io.nghlong3004.game.component.button;

import io.nghlong3004.constant.AudioConstant;
import io.nghlong3004.game.main.GameLogic;
import io.nghlong3004.loader.AudioLoader;
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

    private final AudioLoader audioLoader;

    protected GameButton(int x, int y, int width, int height, int rowIndex, AudioLoader audioLoader) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.box = new Rectangle(x, y, width, height);
        this.rowIndex = rowIndex;
        this.audioLoader = audioLoader;
    }

    protected GameButton(int x, int y, int width, int height, AudioLoader audioLoader) {
        this(x, y, width, height, 0, audioLoader);
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
        audioLoader.playEffect(AudioConstant.TOUCH);
    }

    public boolean isMouseOver(MouseEvent e) {
        return box.contains(e.getX(), e.getY());
    }
}
