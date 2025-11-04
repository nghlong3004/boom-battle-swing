package io.nghlong3004.entity;

import io.nghlong3004.type.SkinType;
import lombok.Getter;
import lombok.Setter;

import java.awt.geom.Rectangle2D;

import static io.nghlong3004.constant.EntityConstant.DOWN;

@Getter
@Setter
public abstract class Entity {
    private boolean up, down, right, left, moving, alive;
    private int direction, tick, index;
    private float speed;
    private SkinType skin;
    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected Rectangle2D.Float box;

    protected Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        initializeHitbox();
        initializeState();
    }

    protected void initializeHitbox() {
        float hitboxWidth = width - 40f;
        float hitboxHeight = height - 11.5f;
        float hitboxX = x + (width - hitboxWidth) / 2;
        float hitboxY = y + (height - hitboxHeight) / 2;
        this.box = new Rectangle2D.Float(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
    }

    protected void initializeState() {
        this.moving = false;
        this.alive = true;
        this.direction = DOWN;
        this.speed = getDefaultSpeed();
        this.tick = 0;
        this.index = 0;
        resetDirection();
    }

    protected abstract float getDefaultSpeed();

    public void reset() {
        initializeHitbox();
        initializeState();
    }

    public void resetDirection() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    protected void setBox(float x, float y, int width, int height) {
        this.box = new Rectangle2D.Float(x, y, width, height);
    }

}
