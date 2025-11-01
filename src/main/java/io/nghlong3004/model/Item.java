package io.nghlong3004.model;

import lombok.Getter;
import lombok.Setter;

import java.awt.geom.Rectangle2D;

@Getter
@Setter
public class Item {
    private final ItemType type;
    private final float x;
    private final float y;
    private final int width;
    private final int height;
    private final Rectangle2D.Float box;
    private boolean collected = false;
    private int animationTick = 0;
    private int animationFrame = 0;

    public Item(ItemType type, float x, float y, int width, int height) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.box = new Rectangle2D.Float(x, y, width, height);
    }

    public void update() {
        animationTick++;
        if (animationTick >= 15) {
            animationFrame = (animationFrame + 1) % 8;
            animationTick = 0;
        }
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        this.collected = true;
    }
}
