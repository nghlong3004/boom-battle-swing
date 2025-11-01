package io.nghlong3004.system;

import io.nghlong3004.model.Bomber;
import io.nghlong3004.model.Entity;
import io.nghlong3004.util.ObjectContainer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EntityRenderSystem extends AbstractEntityRenderSystem {

    @Override
    protected BufferedImage getSprite(Entity entity) {
        int direction = entity.getDirection();
        int index = entity.getIndex();
        var bomberSkins = ObjectContainer.getImageContainer().getBomberSkins();


        if (entity instanceof Bomber bomber) {
            if (bomber.isPlayer()) {
                return bomberSkins.get(GameStateContextHolder.SKIN.index)[direction][index];
            }
            else {
                return bomberSkins.get(bomber.getSkinIndex())[direction][index];
            }
        }

        return bomberSkins.get(GameStateContextHolder.SKIN.index)[direction][index];
    }

    @Override
    protected Color getHitboxColor() {
        return Color.PINK;
    }
}
