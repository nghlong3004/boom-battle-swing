package io.nghlong3004.boom_battle_swing.view.scene.button;

import io.nghlong3004.boom_battle_swing.constant.ButtonConstant;
import io.nghlong3004.boom_battle_swing.constant.ImageConstant;
import io.nghlong3004.boom_battle_swing.util.ImageUtil;
import io.nghlong3004.boom_battle_swing.view.scene.Scene;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SoundButton extends PauseButton implements Scene {

    private BufferedImage[][] soundImages;

    @Getter
    @Setter
    private boolean mouseOver, mousePressed, muted;
    private int rowIndex, columnIndex;

    public SoundButton(int x, int y, int height, int width) {
        super(x, y, height, width);
        loadSoundImages();
    }

    private void loadSoundImages() {
        BufferedImage image = ImageUtil.loadImage(ImageConstant.SOUND_BUTTON);
        soundImages = new BufferedImage[2][3];
        for (int j = 0; j < soundImages.length; ++j) {
            for (int i = 0; i < soundImages[j].length; ++i) {
                soundImages[j][i] = image.getSubimage(i * ButtonConstant.SOUND_BUTTON_SIZE_DEFAULT,
                                                      j * ButtonConstant.SOUND_BUTTON_SIZE_DEFAULT,
                                                      ButtonConstant.SOUND_BUTTON_SIZE_DEFAULT,
                                                      ButtonConstant.SOUND_BUTTON_SIZE_DEFAULT);
            }
        }
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
        g.drawImage(soundImages[rowIndex][columnIndex], x, y, width, height, null);
    }

    public void reset() {
        mouseOver = false;
        mousePressed = false;
    }
}
