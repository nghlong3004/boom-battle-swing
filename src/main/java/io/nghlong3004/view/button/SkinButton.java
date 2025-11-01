package io.nghlong3004.view.button;

import io.nghlong3004.model.BomberSkin;
import io.nghlong3004.util.ImageLoaderUtil;
import io.nghlong3004.view.GameObject;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

import static io.nghlong3004.constant.ImageConstant.BOMBER_AVATAR_TEMPLATE;

@Getter
@Setter
public class SkinButton extends SelectionButton implements GameObject {

    private final BomberSkin skin;

    public SkinButton(int x, int y, int width, int height, BomberSkin skin) {
        super(x, y, width, height);
        this.skin = skin;
        loadImage();
    }

    @Override
    protected void loadImage() {
        String path = BOMBER_AVATAR_TEMPLATE.formatted(skin.name);
        this.image = ImageLoaderUtil.loadImage(path);
    }

    public void reset() {
        mouseOver = false;
        mousePressed = false;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        render(g, skin.name.toUpperCase());
    }
}
