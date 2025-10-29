package io.nghlong3004.boom_battle_swing.view.scene.button;

import io.nghlong3004.boom_battle_swing.constant.ImageConstant;
import io.nghlong3004.boom_battle_swing.util.ImageUtil;
import io.nghlong3004.boom_battle_swing.view.scene.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.boom_battle_swing.constant.ButtonConstant.*;

public class VolumeButton extends AbstractButton implements Scene {

    private BufferedImage[] images;
    private BufferedImage slider;

    private int buttonX = 0;
    private int minSlider, maxSlider;

    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_BUTTON_WIDTH, height, 0);
        hitbox.x -= VOLUME_BUTTON_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        this.minSlider = x + VOLUME_BUTTON_WIDTH / 2;
        this.maxSlider = x + width - VOLUME_BUTTON_WIDTH / 2;
    }

    @Override
    protected void loadImage() {
        images = new BufferedImage[3];
        BufferedImage image = ImageUtil.loadImage(ImageConstant.VOLUM_BUTTON);
        for (int i = 0; i < images.length; ++i) {
            images[i] = image.getSubimage(i * VOLUME_BUTTON_WIDTH_DEFAULT, 0, VOLUME_BUTTON_WIDTH_DEFAULT,
                                          VOLUME_BUTTON_HEIGHT_DEFAULT);
        }
        slider = image.getSubimage(3 * VOLUME_BUTTON_WIDTH_DEFAULT, 0, SLIDER_BUTTON_DEFAULT,
                                   VOLUME_BUTTON_HEIGHT_DEFAULT);
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
    public void draw(Graphics g) {
        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(images[columnIndex], buttonX - VOLUME_BUTTON_WIDTH / 2, y, VOLUME_BUTTON_WIDTH, height, null);
    }

    public void changeButtonX(int buttonX) {
        if (buttonX < minSlider) {
            this.buttonX = minSlider;
        }
        else if (buttonX > maxSlider) {
            this.buttonX = maxSlider;
        }
        else {
            this.buttonX = buttonX;
        }
        hitbox.x = buttonX - VOLUME_BUTTON_WIDTH / 2;
    }
}
