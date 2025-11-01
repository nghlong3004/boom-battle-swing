package io.nghlong3004.system;

import io.nghlong3004.model.Item;
import io.nghlong3004.util.ImageContainer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ItemRenderSystem implements RenderSystem {

    @Override
    public void render(Graphics g, Object object) {
        if (!(object instanceof Item item)) {
            return;
        }

        if (item.isCollected()) {
            return;
        }

        BufferedImage itemImage = ImageContainer.getItemImage(item.getType());
        if (itemImage != null) {
            g.drawImage(itemImage, (int) item.getX(), (int) item.getY(), item.getWidth(), item.getHeight(), null);
        }

        BufferedImage effectImage = ImageContainer.getItemEffectFrame(item.getAnimationFrame());
        if (effectImage != null) {
            g.drawImage(effectImage, (int) item.getX(), (int) item.getY(), item.getWidth(), item.getHeight(), null);
        }
    }
}
