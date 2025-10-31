package io.nghlong3004.boom_battle_swing.view.render;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public interface Renderer {
    void render(Graphics g, Object entity);

    void drawHitbox(Graphics g, Rectangle2D.Float hitbox);
}
