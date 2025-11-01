package io.nghlong3004.system;

import io.nghlong3004.constant.BomberConstant;
import io.nghlong3004.model.Entity;
import io.nghlong3004.util.ObjectContainer;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class EntityRenderSystem implements RenderSystem {

    @Override
    public void render(Graphics g, Object entity) {
        Entity bomber = (Entity) entity;
        int direction = bomber.getDirection();
        int index = bomber.getIndex();
        Rectangle2D.Float hitbox = bomber.getBox();
        var bomberSkins = ObjectContainer.getImageContainer().getBomberSkins();
        g.drawImage(bomberSkins.get(GameStateContextHolder.SKIN.index)[direction][index],
                    (int) (hitbox.x - BomberConstant.X_DRAW_OFF_SET - 6),
                    (int) (hitbox.y - BomberConstant.Y_DRAW_OFF_SET), bomber.getWidth(), bomber.getHeight(), null);
    }

}
