package io.nghlong3004.view.button;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class GameButton {

    @Getter
    @Setter
    protected boolean mousePressed, mouseOver;
    protected int x, y, width, height;
    protected int rowIndex, columnIndex;
    protected Rectangle box;

    protected GameButton(int x, int y, int width, int height, int rowIndex) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.box = new Rectangle(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImage();
    }

    protected GameButton(int x, int y, int width, int height) {
        this(x, y, width, height, 0);
    }
    

    protected abstract void loadImage();

    public void reset() {
        setMouseOver(false);
        setMousePressed(false);
    }

    public boolean isMouseOver(MouseEvent e) {
        return box.contains(e.getX(), e.getY());
    }

}
