package io.nghlong3004.view.button;

import io.nghlong3004.constant.ImageConstant;
import io.nghlong3004.util.ImageLoaderUtil;
import io.nghlong3004.view.GameObject;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.AudioConstant.VOLUME_START;
import static io.nghlong3004.constant.ButtonConstant.*;

public class VolumeButton extends GameButton implements GameObject {

    private BufferedImage[] images;
    private BufferedImage slider;

    @Getter
    private float floatValue;
    private final int minSlider;
    private final int maxSlider;
    private int buttonX;


    public VolumeButton(int x, int y, int width, int height) {
        super((int) (x + width * VOLUME_START), y, VOLUME_BUTTON_WIDTH, height, 0);
        this.box.x -= (int) (VOLUME_BUTTON_WIDTH * 0.5);
        buttonX = (int) (x + width * VOLUME_START);
        this.x = x;
        this.width = width;
        this.minSlider = x + VOLUME_BUTTON_WIDTH / 2;
        this.maxSlider = x + width - VOLUME_BUTTON_WIDTH / 2;
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
        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(images[columnIndex], (int) (buttonX - VOLUME_BUTTON_WIDTH * 0.5), y, VOLUME_BUTTON_WIDTH, height,
                    null);
    }

    public void changeButtonX(int buttonX) {
        if (buttonX < minSlider) {
            this.buttonX = minSlider;
        }
        else {
            this.buttonX = Math.min(buttonX, maxSlider);
        }
        updateFloatValue();
        box.x = buttonX - VOLUME_BUTTON_WIDTH / 2;
    }

    private void updateFloatValue() {
        float range = maxSlider - minSlider;
        float value = buttonX - minSlider;
        floatValue = value / range;
    }

    @Override
    protected void loadImage() {
        images = new BufferedImage[3];
        BufferedImage image = ImageLoaderUtil.loadImage(ImageConstant.VOLUME_BUTTON);
        for (int i = 0; i < images.length; ++i) {
            images[i] = image.getSubimage(i * VOLUME_BUTTON_WIDTH_DEFAULT, 0, VOLUME_BUTTON_WIDTH_DEFAULT,
                                          VOLUME_BUTTON_HEIGHT_DEFAULT);
        }
        slider = image.getSubimage(3 * VOLUME_BUTTON_WIDTH_DEFAULT, 0, SLIDER_BUTTON_DEFAULT,
                                   VOLUME_BUTTON_HEIGHT_DEFAULT);
    }

}
