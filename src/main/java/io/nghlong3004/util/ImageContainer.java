package io.nghlong3004.util;

import io.nghlong3004.model.ItemType;
import lombok.Getter;

import java.awt.image.BufferedImage;
import java.util.List;

@Getter
public class ImageContainer {
    private final List<BufferedImage[][]> bomberSkins;
    private final BufferedImage[][] tileSprites;
    private final BufferedImage[] bombSprites;
    private final BufferedImage[] explosionSprites;
    private final BufferedImage[] explosionAnimationFrames;
    private final BufferedImage[][][] soldierSprites;
    private final BufferedImage[] bomberDeathSprites;
    private final BufferedImage[] itemImages;
    private final BufferedImage[] itemEffectFrames;

    public static ImageContainer getInstance() {
        return Holder.INSTANCE;
    }

    private ImageContainer() {
        bomberSkins = ImageLoaderUtil.loadBomberSkin();
        tileSprites = ImageLoaderUtil.loadTileSprites();
        bombSprites = ImageLoaderUtil.loadBombSprites();
        explosionSprites = ImageLoaderUtil.loadExplosionSprites();
        explosionAnimationFrames = ImageLoaderUtil.loadExplosionAnimationFrames();
        soldierSprites = ImageLoaderUtil.loadSoldierSprites();
        bomberDeathSprites = ImageLoaderUtil.loadBomberDeathSprites();
        itemImages = ImageLoaderUtil.loadItemImages();
        itemEffectFrames = ImageLoaderUtil.loadItemEffectFrames();
    }

    public static BufferedImage getItemImage(ItemType type) {
        return getInstance().getItemImages()[type.index];
    }

    public static BufferedImage getItemEffectFrame(int frame) {
        BufferedImage[] frames = getInstance().getItemEffectFrames();
        if (frame >= 0 && frame < frames.length) {
            return frames[frame];
        }
        return null;
    }

    private static class Holder {
        private static final ImageContainer INSTANCE = new ImageContainer();
    }
}
