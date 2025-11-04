package io.nghlong3004.component.button;

import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.type.GameType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.ButtonConstant.MENU_BUTTON_HEIGHT;
import static io.nghlong3004.constant.ButtonConstant.MENU_BUTTON_WIDTH;
import static io.nghlong3004.constant.ImageConstant.BUTTON_EMPTY;

@Slf4j
public class ModeButton extends GameButton {
    @Getter
    private GameType gameMode;
    private BufferedImage buttonImage;
    private final String label;

    public ModeButton(int x, int y, GameType gameMode, String label) {
        super(x - MENU_BUTTON_WIDTH / 2, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, 0);
        this.gameMode = gameMode;
        this.label = label;
        loadImage();
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(buttonImage, x, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, null);
        if (mousePressed) {
            g.setColor(new Color(0, 0, 0, 80));
            g.fillRect(x, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        }
        else if (mouseOver) {
            g.setColor(new Color(255, 255, 255, 50));
            g.fillRect(x, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(label);
        int textX = x + (MENU_BUTTON_WIDTH - textWidth) / 2;
        int textY = y + (MENU_BUTTON_HEIGHT + fm.getAscent()) / 2 - 4;

        g.setColor(Color.BLACK);
        g.drawString(label, textX + 2, textY + 2);

        g.setColor(Color.WHITE);
        g.drawString(label, textX, textY);
    }

    @Override
    protected void loadImage() {
        buttonImage = ImageLoader.loadImage(BUTTON_EMPTY);
        if (buttonImage == null) {
            log.error("Failed to load button_empty.png!");
        }
    }
}
