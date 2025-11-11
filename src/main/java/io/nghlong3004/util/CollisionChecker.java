package io.nghlong3004.util;

import io.nghlong3004.model.entities.Bomber;

import java.awt.geom.Rectangle2D;

public interface CollisionChecker {
    boolean canMoveTo(Rectangle2D.Float newHitbox, Bomber currentBomber);

    boolean isCollidingWithSolid(float x, float y, float width, float height);
}
