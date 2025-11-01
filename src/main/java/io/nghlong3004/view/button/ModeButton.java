package io.nghlong3004.view.button;

import io.nghlong3004.model.GameMode;
import io.nghlong3004.util.ImageLoaderUtil;
import io.nghlong3004.view.GameObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.ButtonConstant.*;
import static io.nghlong3004.constant.ImageConstant.MENU_BUTTON;

/**
 * Button for selecting game mode (Online/Offline)
 */
@Slf4j
public class ModeButton extends GameButton implements GameObject {

    @Getter
    private GameMode gameMode;
    private BufferedImage[] images;
    private String label;

    public ModeButton(int x, int y, int rowIndex, GameMode gameMode, String label) {
        super(x - MENU_BUTTON_WIDTH / 2, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, rowIndex);
        this.gameMode = gameMode;
        this.label = label;
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
        
        // Draw label text on button
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(label);
        int textX = x + (MENU_BUTTON_WIDTH - textWidth) / 2;
        int textY = y + (MENU_BUTTON_HEIGHT + fm.getAscent()) / 2 - 2;
        g.drawString(label, textX, textY);
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
