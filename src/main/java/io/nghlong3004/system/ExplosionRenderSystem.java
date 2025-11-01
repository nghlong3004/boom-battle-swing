package io.nghlong3004.system;

import io.nghlong3004.model.Explosion;
import io.nghlong3004.util.ObjectContainer;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.GameConstant.TILES_SIZE;

@Slf4j
public class ExplosionRenderSystem implements RenderSystem {
    
    private final BufferedImage[] explosionAnimationFrames;
    
    public ExplosionRenderSystem() {
        this.explosionAnimationFrames = ObjectContainer.getImageContainer().getExplosionAnimationFrames();
        log.debug("ExplosionRenderSystem initialized with {} animation frames", explosionAnimationFrames.length);
    }
    
    @Override
    public void render(Graphics g, Object entity) {
        Explosion explosion = (Explosion) entity;
        
        if (explosion.isFinished() || explosion.getIndex() >= explosion.getMaxFrames()) {
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        int currentFrame = explosion.getIndex();
        
        for (Explosion.ExplosionTile tile : explosion.getExplosionTiles()) {
            BufferedImage frame = explosionAnimationFrames[currentFrame];
            g2d.drawImage(frame, 
                         (int) tile.getX(), 
                         (int) tile.getY(), 
                         TILES_SIZE, 
                         TILES_SIZE, 
                         null);
        }
    }
}
