package io.nghlong3004.boom_battle_swing.view.scene.button;

import io.nghlong3004.boom_battle_swing.constant.ButtonConstant;
import io.nghlong3004.boom_battle_swing.constant.ImageConstant;
import io.nghlong3004.boom_battle_swing.util.ImageUtil;
import io.nghlong3004.boom_battle_swing.view.scene.Scene;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SoundButton extends AbstractButton implements Scene {

    @Getter
    @Setter
    private boolean muted;
    private BufferedImage[][] images;

    public SoundButton(int x, int y, int height, int width) {
        super(x, y, width, height, 0);
    }

    @Override
    public void update() {
        if (muted) {
            rowIndex = 1;
        }
        else {
            rowIndex = 0;
        }
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
        g.drawImage(images[rowIndex][columnIndex], x, y, width, height, null);
    }

    @Override
    protected void loadImage() {
        BufferedImage image = ImageUtil.loadImage(ImageConstant.SOUND_BUTTON);
        images = new BufferedImage[2][3];
        for (int i = 0; i < images.length; ++i) {
            for (int j = 0; j < images[i].length; ++j) {
                images[i][j] = image.getSubimage(j * ButtonConstant.SOUND_BUTTON_SIZE_DEFAULT,
                                                 i * ButtonConstant.SOUND_BUTTON_SIZE_DEFAULT,
                                                 ButtonConstant.SOUND_BUTTON_SIZE_DEFAULT,
                                                 ButtonConstant.SOUND_BUTTON_SIZE_DEFAULT);
            }
        }
    }
}
