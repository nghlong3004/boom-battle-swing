package io.nghlong3004.boom_battle_swing.view.scene.button;

import lombok.Data;

import java.awt.*;

@Data
public abstract class AbstractButton {
    protected int x, y, width, height;
    protected int rowIndex, columnIndex;
    protected Rectangle hitbox;
    protected boolean mousePressed, mouseOver;

    protected AbstractButton(int x, int y, int width, int height, int rowIndex) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.hitbox = new Rectangle(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImage();
    }

    protected abstract void loadImage();

    public void reset() {
        this.mouseOver = false;
        this.mousePressed = false;
    }

}
