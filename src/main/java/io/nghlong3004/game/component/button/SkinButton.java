package io.nghlong3004.game.component.button;

import io.nghlong3004.loader.AudioLoader;
import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.model.type.SkinType;
import lombok.Getter;

import java.awt.*;

import static io.nghlong3004.constant.ImageConstant.BOMBER_AVATAR_TEMPLATE;

public class SkinButton extends MapButton {
    @Getter
    private final SkinType skin;

    public SkinButton(int x, int y, int width, int height, SkinType skin, AudioLoader audioLoader) {
        super(x, y, width, height, null, audioLoader);
        this.skin = skin;
        loadImage();
    }

    @Override
    protected void loadImage() {
        if (skin == null) {
            return;
        }
        String path = BOMBER_AVATAR_TEMPLATE.formatted(skin.getAssetKey());
        this.image = ImageLoader.loadImage(path);
    }

    public void reset() {
        mouseOver = false;
        mousePressed = false;
    }

    @Override
    public void render(Graphics g) {
        render(g, skin.getAssetKey());
    }
}
