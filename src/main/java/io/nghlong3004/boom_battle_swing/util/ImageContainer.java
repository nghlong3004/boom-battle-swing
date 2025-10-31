package io.nghlong3004.boom_battle_swing.util;

import lombok.Getter;

import java.awt.image.BufferedImage;
import java.util.List;

@Getter
public class ImageContainer {
    private final List<BufferedImage[][]> bomberSkins;
    private BufferedImage[][] tileSprites;

    public static ImageContainer getInstance() {
        return Holder.INSTANCE;
    }

    private ImageContainer() {
        bomberSkins = ImageLoaderUtil.loadBomberSkin();
        tileSprites = ImageLoaderUtil.loadTileSprites();
    }

    private static class Holder {
        private static final ImageContainer INSTANCE = new ImageContainer();
    }
}
