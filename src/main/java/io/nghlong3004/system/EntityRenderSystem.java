package io.nghlong3004.system;

import io.nghlong3004.model.Bomber;
import io.nghlong3004.model.Entity;
import io.nghlong3004.util.ObjectContainer;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class EntityRenderSystem extends AbstractEntityRenderSystem {

    @Override
    protected BufferedImage getSprite(Entity entity) {
        int direction = entity.getDirection();
        int index = entity.getIndex();
        var bomberSkins = ObjectContainer.getImageContainer().getBomberSkins();

        if (entity instanceof Bomber bomber) {

            if (bomber.isDead()) {
                var deathSprites = ObjectContainer.getImageContainer().getBomberDeathSprites();
                int frameIndex = (bomber.getDeathAnimationFrame() + 1) % deathSprites.length;
                return deathSprites[frameIndex];
            }


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

    @Override
    protected void applyDeathEffect(Graphics2D g2d, Entity entity) {


        if (!entity.isAlive() && !(entity instanceof Bomber bomber && bomber.isDead())) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
    }

    @Override
    protected void resetDeathEffect(Graphics2D g2d, Entity entity) {

        if (!entity.isAlive() && !(entity instanceof Bomber bomber && bomber.isDead())) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    @Override
    protected void drawDeathLabel(Graphics2D g2d, Entity entity, Rectangle2D.Float hitbox) {

        if (!entity.isAlive() && !(entity instanceof Bomber)) {
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, getDeathLabelFontSize()));
            String deathText = "DEAD";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(deathText);
            g2d.drawString(deathText, (int) hitbox.x + (int) (hitbox.width - textWidth) / 2, (int) hitbox.y - 10);
        }
    }
}
