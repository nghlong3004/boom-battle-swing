package io.nghlong3004.util;

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
    }

    private static class Holder {
        private static final ImageContainer INSTANCE = new ImageContainer();
    }
}
