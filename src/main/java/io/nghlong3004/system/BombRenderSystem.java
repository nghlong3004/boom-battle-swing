package io.nghlong3004.system;

import io.nghlong3004.model.Bomb;
import io.nghlong3004.util.ObjectContainer;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;

@Slf4j
public class BombRenderSystem implements RenderSystem {

    private final BufferedImage[] bombSprites;

    public BombRenderSystem() {
        this.bombSprites = ObjectContainer.getImageContainer().getBombSprites();
        log.debug("BombRenderSystem initialized with {} sprite frames", bombSprites.length);
    }

    @Override
    public void render(Graphics g, Object entity) {
        Bomb bomb = (Bomb) entity;

        if (bomb.isExploded()) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;

        int frameIndex = bomb.getIndex() % bombSprites.length;
        BufferedImage bombImage = bombSprites[frameIndex];

        g2d.drawImage(bombImage, (int) bomb.getX(), (int) bomb.getY(), bomb.getWidth(), bomb.getHeight(), null);

    }
}
