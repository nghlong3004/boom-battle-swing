package io.nghlong3004.assets;

import io.nghlong3004.loader.MapLoader;
import lombok.Getter;

import java.awt.image.BufferedImage;

public class MapAssets {
    @Getter
    private final BufferedImage[][] mapAssets;

    public static MapAssets getInstance() {
        return Holder.INSTANCE;
    }

    private MapAssets() {
        mapAssets = MapLoader.loadMapAssets();
    }

    private static class Holder {
        private static final MapAssets INSTANCE = new MapAssets();
    }

}
