package io.nghlong3004.view.button;

import io.nghlong3004.constant.ImageConstant;
import io.nghlong3004.util.ImageLoaderUtil;
import io.nghlong3004.view.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.ButtonConstant.URM_BUTTON_SIZE;
import static io.nghlong3004.constant.ButtonConstant.URM_BUTTON_SIZE_DEFAULT;

public class SpriteButton extends GameButton implements GameObject {

    private BufferedImage[] images;

    public SpriteButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height, rowIndex);
    }

    @Override
    public void update() {
        columnIndex = 0;
        if (mouseOver) {
            columnIndex = 1;
        }
        if (mousePressed) {
            columnIndex = 2;
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(images[columnIndex], x, y, URM_BUTTON_SIZE, URM_BUTTON_SIZE, null);
    }

    @Override
    protected void loadImage() {
        images = new BufferedImage[3];
        BufferedImage image = ImageLoaderUtil.loadImage(ImageConstant.URM_BUTTON);
        for (int i = 0; i < images.length; ++i) {
            images[i] = image.getSubimage(i * URM_BUTTON_SIZE_DEFAULT, rowIndex * URM_BUTTON_SIZE_DEFAULT,
                                          URM_BUTTON_SIZE_DEFAULT, URM_BUTTON_SIZE_DEFAULT);
        }
    }
}
