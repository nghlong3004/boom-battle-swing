package io.nghlong3004.boom_battle_swing.view.render;

import io.nghlong3004.boom_battle_swing.constant.BomberConstant;
import io.nghlong3004.boom_battle_swing.model.Bomber;
import io.nghlong3004.boom_battle_swing.model.BomberSkin;
import io.nghlong3004.boom_battle_swing.util.ObjectContainer;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.geom.Rectangle2D;

@Slf4j
public class BomberRenderer implements Renderer {

    public BomberRenderer() {
    }

    @Override
    public void render(Graphics g, Object entity) {
        Bomber bomber = (Bomber) entity;
        int direction = bomber.getDirection();
        int index = bomber.getAnimationIndex();
        Rectangle2D.Float hitbox = bomber.getHitbox();
        var bomberSkins = ObjectContainer.getImageContainer().getBomberSkins();
        g.drawImage(bomberSkins.get(BomberSkin.SKIN.index)[direction][index],
                    (int) (hitbox.x - BomberConstant.X_DRAW_OFF_SET - 6),
                    (int) (hitbox.y - BomberConstant.Y_DRAW_OFF_SET), bomber.getWidth(), bomber.getHeight(), null);
    }

    @Override
    public void drawHitbox(Graphics g, Rectangle2D.Float hitbox) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }
}
