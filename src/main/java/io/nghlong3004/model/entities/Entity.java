package io.nghlong3004.model.entities;

import io.nghlong3004.model.type.SkinType;
import lombok.Getter;
import lombok.Setter;

import java.awt.geom.Rectangle2D;

import static io.nghlong3004.constant.EntityConstant.*;

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

    protected Entity(float x, float y, int width, int height, SkinType skinType) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.skin = skinType;
        float hitboxX = x + HITBOX_OFFSET_X;
        float hitboxY = y + HITBOX_OFFSET_Y;
        this.box = new Rectangle2D.Float(hitboxX, hitboxY, HITBOX_WIDTH, HITBOX_HEIGHT);
        initializeState();
    }

    protected void updateHitboxPosition() {
        this.box.x = x + HITBOX_OFFSET_X;
        this.box.y = y + HITBOX_OFFSET_Y;
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
        updateHitboxPosition();
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
