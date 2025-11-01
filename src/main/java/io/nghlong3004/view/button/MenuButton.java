package io.nghlong3004.view.button;

import io.nghlong3004.model.State;
import io.nghlong3004.util.ImageLoaderUtil;
import io.nghlong3004.view.GameObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.ButtonConstant.*;
import static io.nghlong3004.constant.ImageConstant.MENU_BUTTON;


@Slf4j
public class MenuButton extends GameButton implements GameObject {

    @Getter
    private State state;
    private BufferedImage[] images;

    public MenuButton(int x, int y, int rowIndex, State state) {
        super(x - MENU_BUTTON_WIDTH / 2, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, rowIndex);
        this.state = state;
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
        g.drawImage(images[columnIndex], x, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, null);
    }

    @Override
    protected void loadImage() {
        images = new BufferedImage[3];
        BufferedImage image = ImageLoaderUtil.loadImage(MENU_BUTTON);
        for (int i = 0; i < images.length; ++i) {
            images[i] = image.getSubimage(i * MENU_BUTTON_WIDTH_DEFAULT, rowIndex * MENU_BUTTON_HEIGHT_DEFAULT,
                                          MENU_BUTTON_WIDTH_DEFAULT, MENU_BUTTON_HEIGHT_DEFAULT);
        }
    }
}
