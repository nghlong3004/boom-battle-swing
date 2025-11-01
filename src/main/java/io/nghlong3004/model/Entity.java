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
    private boolean alive;
    @Setter
    private int direction;
    @Setter
    private int tick;
    @Setter
    private int index;
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
