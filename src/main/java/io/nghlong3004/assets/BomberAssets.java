package io.nghlong3004.assets;

import io.nghlong3004.loader.BomberLoader;
import lombok.Getter;

import java.awt.image.BufferedImage;
import java.util.List;

@Getter
public class BomberAssets {
    private final List<BufferedImage[][]> bomberAssets;
    private final BufferedImage[] bomberDeathAssets;

    public static BomberAssets getInstance() {
        return BomberAssets.Holder.INSTANCE;
    }

    private BomberAssets() {
        bomberAssets = BomberLoader.loadBomberAssets();
        bomberDeathAssets = BomberLoader.loadBomberDeathAssets();
    }

    private static class Holder {
        private static final BomberAssets INSTANCE = new BomberAssets();
    }
}
