package io.nghlong3004.boom_battle_swing.view.scene.button;

import io.nghlong3004.boom_battle_swing.util.ImageUtil;
import io.nghlong3004.boom_battle_swing.view.scene.GameState;
import io.nghlong3004.boom_battle_swing.view.scene.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.boom_battle_swing.constant.ButtonConstant.*;
import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.MENU_BUTTON;

public class MenuButton extends AbstractButton implements Scene {

    private final GameState state;

    private BufferedImage[] images;

    public MenuButton(int x, int y, int rowIndex, GameState state) {
        super(x - MENU_BUTTON_WIDTH / 2, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, rowIndex);
        this.state = state;
    }

    public void applyGameState() {
        GameState.state = state;
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
    public void draw(Graphics graphics) {
        graphics.drawImage(images[columnIndex], x, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, null);
    }

    @Override
    protected void loadImage() {
        images = new BufferedImage[3];
        BufferedImage image = ImageUtil.loadImage(MENU_BUTTON);
        for (int i = 0; i < images.length; ++i) {
            images[i] = image.getSubimage(i * MENU_BUTTON_WIDTH_DEFAULT, rowIndex * MENU_BUTTON_HEIGHT_DEFAULT,
                                          MENU_BUTTON_WIDTH_DEFAULT, MENU_BUTTON_HEIGHT_DEFAULT);
        }
    }
}
