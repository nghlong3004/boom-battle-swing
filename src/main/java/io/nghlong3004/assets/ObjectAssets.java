package io.nghlong3004.assets;

import io.nghlong3004.loader.ObjectLoader;
import lombok.Getter;

import java.awt.image.BufferedImage;

@Getter
public class ObjectAssets {

    private final BufferedImage[] bombAssets;

    private final BufferedImage[] explosionAssets;
    private final BufferedImage[] explosionAnimationFrameAssets;

    private final BufferedImage[] itemAssets;
    private final BufferedImage[] itemEffectFramesAssets;
    private final BufferedImage[] moveEffectAssets;


    public static ObjectAssets getInstance() {
        return ObjectAssets.Holder.INSTANCE;
    }

    private ObjectAssets() {
        bombAssets = ObjectLoader.loadBombAssets();
        explosionAssets = ObjectLoader.loadExplosionAssets();
        explosionAnimationFrameAssets = ObjectLoader.loadExplosionAnimationFrameAssets();
        itemAssets = ObjectLoader.loadItemAssets();
        itemEffectFramesAssets = ObjectLoader.loadItemEffectFramesAssets();
        moveEffectAssets = ObjectLoader.loadMoveEffectAssets();
    }

    private static class Holder {
        private static final ObjectAssets INSTANCE = new ObjectAssets();
    }

}
