package io.nghlong3004.model;

import lombok.Getter;
import lombok.Setter;

import java.awt.geom.Rectangle2D;

import static io.nghlong3004.constant.BomberConstant.BOMBER_SPEED;
import static io.nghlong3004.constant.BomberConstant.DOWN;

@Getter
public abstract class Entity {

    @Setter
    private boolean up, down, right, left;
    @Setter
    private boolean moving;
    @Setter
    private int direction;
    // This is the tick used to do UPS
    @Setter
    private int tick;
    // This is index for images entity
    @Setter
    private int index;
    // This is speed for entity
    @Setter
    private float speed;
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
        reset();
    }

    public void reset() {
        setBox(x, y, (int) (width - 27.5f), (int) (height - 11.5f));
        this.moving = false;
        this.direction = DOWN;
        this.speed = BOMBER_SPEED;
        resetDirection();
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
