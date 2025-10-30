package io.nghlong3004.boom_battle_swing.view.render;

import io.nghlong3004.boom_battle_swing.model.Bomber;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public interface Renderer {
    void render(Graphics g, Bomber bomber);

    void drawHitbox(Graphics g, Rectangle2D.Float hitbox);
}
