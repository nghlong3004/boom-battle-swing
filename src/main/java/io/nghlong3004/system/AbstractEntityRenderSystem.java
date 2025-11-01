package io.nghlong3004.system;

import io.nghlong3004.model.Entity;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class AbstractEntityRenderSystem implements RenderSystem {
    
    @Override
    public void render(Graphics g, Object entity) {
        Entity e = (Entity) entity;
        Graphics2D g2d = (Graphics2D) g;
        Rectangle2D.Float hitbox = e.getBox();
        
        applyDeathEffect(g2d, e);
        
        BufferedImage sprite = getSprite(e);
        if (sprite != null) {
            drawSprite(g2d, sprite, hitbox);
        }
        
        resetDeathEffect(g2d, e);
        drawDeathLabel(g2d, e, hitbox);
        drawHitbox(g, hitbox);
    }
    
    protected abstract BufferedImage getSprite(Entity entity);
    
    protected abstract Color getHitboxColor();
    
    protected void applyDeathEffect(Graphics2D g2d, Entity entity) {
        if (!entity.isAlive()) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
    }
    
    protected void resetDeathEffect(Graphics2D g2d, Entity entity) {
        if (!entity.isAlive()) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }
    
    protected void drawSprite(Graphics2D g2d, BufferedImage sprite, Rectangle2D.Float hitbox) {
        g2d.drawImage(sprite, 
                     (int) hitbox.x - 5, 
                     (int) hitbox.y - 5, 
                     (int) hitbox.width + 10, 
                     (int) hitbox.height + 10, 
                     null);
    }
    
    protected void drawDeathLabel(Graphics2D g2d, Entity entity, Rectangle2D.Float hitbox) {
        if (!entity.isAlive()) {
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, getDeathLabelFontSize()));
            String deathText = "DEAD";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(deathText);
            g2d.drawString(deathText, 
                          (int) hitbox.x + (int) (hitbox.width - textWidth) / 2, 
                          (int) hitbox.y - 10);
        }
    }
    
    protected void drawHitbox(Graphics g, Rectangle2D.Float hitbox) {
        g.setColor(getHitboxColor());
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }
    
    protected int getDeathLabelFontSize() {
        return 20;
    }
}
