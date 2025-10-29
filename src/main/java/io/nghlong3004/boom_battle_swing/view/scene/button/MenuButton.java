package io.nghlong3004.boom_battle_swing.view.scene.button;

import io.nghlong3004.boom_battle_swing.util.ImageUtil;
import io.nghlong3004.boom_battle_swing.view.scene.GameState;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.boom_battle_swing.constant.ButtonConstant.*;
import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.MENU_BUTTON;

public class MenuButton {

    @Getter
    @Setter
    private boolean mousePressed, mouseOver;

    private final int x, y;
    private final int rIndex;
    private int index;
    private final int xOffsetCenter;
    private final GameState state;
    @Getter
    private Rectangle bounds;

    private Rectangle hitbox;

    private BufferedImage[] images;

    public MenuButton(int x, int y, int rIndex, GameState state) {
        this.x = x;
        this.y = y;
        this.rIndex = rIndex;
        this.state = state;
        this.mousePressed = false;
        this.mouseOver = false;
        this.xOffsetCenter = MENU_BUTTON_WIDTH / 2;
        this.bounds = new Rectangle(x - xOffsetCenter, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        loadImages();
    }

    public void applyGameState() {
        GameState.state = state;
    }

    public void update() {
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }
    }

    public void draw(Graphics graphics) {
        graphics.drawImage(images[index], x - xOffsetCenter, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, null);
    }

    public void reset() {
        setMouseOver(false);
        setMousePressed(false);
    }

    private void loadImages() {
        images = new BufferedImage[3];
        BufferedImage image = ImageUtil.loadImage(MENU_BUTTON);
        for (int i = 0; i < images.length; ++i) {
            images[i] = image.getSubimage(i * MENU_BUTTON_WIDTH_DEFAULT, rIndex * MENU_BUTTON_HEIGHT_DEFAULT,
                                          MENU_BUTTON_WIDTH_DEFAULT, MENU_BUTTON_HEIGHT_DEFAULT);
        }
    }
}
