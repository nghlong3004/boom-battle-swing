package io.nghlong3004.boom_battle_swing.model;

import lombok.AllArgsConstructor;

import java.awt.*;

@AllArgsConstructor
public abstract class Entity {

    protected float x;
    protected float y;

    public abstract void update();

    public abstract void render(Graphics graphics);

}
