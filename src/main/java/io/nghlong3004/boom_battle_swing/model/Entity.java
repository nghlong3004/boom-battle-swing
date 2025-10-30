package io.nghlong3004.boom_battle_swing.model;

import lombok.Getter;

import java.awt.geom.Rectangle2D;

public abstract class Entity {

    protected float x;
    protected float y;
    @Getter
    protected int width;
    @Getter
    protected int height;
    @Getter
    protected Rectangle2D.Float hitbox;

    protected Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void setHitbox(float x, float y, int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

}
