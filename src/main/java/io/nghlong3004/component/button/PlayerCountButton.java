package io.nghlong3004.component.button;

import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.type.PlayerCountType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.ButtonConstant.MENU_BUTTON_HEIGHT;
import static io.nghlong3004.constant.ButtonConstant.MENU_BUTTON_WIDTH;

@Slf4j
public class PlayerCountButton extends GameButton {
    @Getter
    private final PlayerCountType playerCount;
    private BufferedImage buttonNormal;
    private BufferedImage buttonTouch;

    @Getter
    private boolean isBlinking;
    private long blinkStartTime;
    private static final long BLINK_DURATION = 300;
    private static final int BLINK_INTERVAL = 100;

    public PlayerCountButton(int x, int y, PlayerCountType playerCount, String normalImagePath, String touchImagePath) {
        super(x - MENU_BUTTON_WIDTH / 2, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, 0);
        this.playerCount = playerCount;
        this.isBlinking = false;
        loadImages(normalImagePath, touchImagePath);
    }

    @Override
    public void update() {
        if (isBlinking && System.currentTimeMillis() - blinkStartTime > BLINK_DURATION) {
            isBlinking = false;
        }
    }

    @Override
    public void render(Graphics g) {
        BufferedImage currentImage;

        if (isBlinking) {
            long elapsed = System.currentTimeMillis() - blinkStartTime;
            boolean showTouch = (elapsed / BLINK_INTERVAL) % 2 == 0;
            currentImage = showTouch ? buttonTouch : buttonNormal;
        }
        else if (mouseOver || mousePressed) {
            currentImage = buttonTouch;
        }
        else {
            currentImage = buttonNormal;
        }

        g.drawImage(currentImage, x, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, null);
    }

    @Override
    protected void loadImage() {
    }

    private void loadImages(String normalImagePath, String touchImagePath) {
        buttonNormal = ImageLoader.loadImage(normalImagePath);
        buttonTouch = ImageLoader.loadImage(touchImagePath);

        if (buttonNormal == null) {
            log.error("Failed to load button image: {}", normalImagePath);
        }
        if (buttonTouch == null) {
            log.error("Failed to load button touch image: {}", touchImagePath);
        }
    }

    public void startBlinking() {
        isBlinking = true;
        blinkStartTime = System.currentTimeMillis();
    }
}
