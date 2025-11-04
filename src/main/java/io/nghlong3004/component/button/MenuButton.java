package io.nghlong3004.component.button;

import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.type.GameStateType;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.ButtonConstant.*;
import static io.nghlong3004.constant.ImageConstant.MENU_BUTTON;

public class MenuButton extends GameButton {
    @Getter
    private GameStateType state;
    private BufferedImage[] images;

    public MenuButton(int x, int y, int rowIndex, GameStateType state) {
        super(x, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, rowIndex);
        this.state = state;
        loadImage();
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
        BufferedImage image = ImageLoader.loadImage(MENU_BUTTON);
        for (int i = 0; i < images.length; ++i) {
            images[i] = image.getSubimage(i * MENU_BUTTON_WIDTH_DEFAULT, rowIndex * MENU_BUTTON_HEIGHT_DEFAULT,
                                          MENU_BUTTON_WIDTH_DEFAULT, MENU_BUTTON_HEIGHT_DEFAULT);
        }
    }
}
