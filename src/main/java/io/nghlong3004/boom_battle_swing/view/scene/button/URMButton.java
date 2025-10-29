package io.nghlong3004.boom_battle_swing.view.scene.button;

import io.nghlong3004.boom_battle_swing.constant.ImageConstant;
import io.nghlong3004.boom_battle_swing.util.ImageUtil;
import io.nghlong3004.boom_battle_swing.view.scene.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.boom_battle_swing.constant.ButtonConstant.URM_BUTTON_SIZE;
import static io.nghlong3004.boom_battle_swing.constant.ButtonConstant.URM_BUTTON_SIZE_DEFAULT;

public class URMButton extends AbstractButton implements Scene {

    private BufferedImage[] images;

    public URMButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height, rowIndex);
        this.rowIndex = rowIndex;
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
        g.drawImage(images[columnIndex], x, y, URM_BUTTON_SIZE, URM_BUTTON_SIZE, null);
    }

    @Override
    protected void loadImage() {
        images = new BufferedImage[3];
        BufferedImage image = ImageUtil.loadImage(ImageConstant.URM_BUTTON);
        for (int i = 0; i < images.length; ++i) {
            images[i] = image.getSubimage(i * URM_BUTTON_SIZE_DEFAULT, rowIndex * URM_BUTTON_SIZE_DEFAULT,
                                          URM_BUTTON_SIZE_DEFAULT, URM_BUTTON_SIZE_DEFAULT);
        }
    }
}
