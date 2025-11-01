package io.nghlong3004.view.button;

import io.nghlong3004.model.GameMode;
import io.nghlong3004.util.ImageLoaderUtil;
import io.nghlong3004.view.GameObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.ButtonConstant.*;
import static io.nghlong3004.constant.ImageConstant.BUTTON_EMPTY;

/**
 * Button for selecting game mode (Online/Offline)
 * Uses button_empty.png as background with text overlay
 */
@Slf4j
public class ModeButton extends GameButton implements GameObject {

    @Getter
    private GameMode gameMode;
    private BufferedImage buttonImage;
    private String label;

    public ModeButton(int x, int y, GameMode gameMode, String label) {
        super(x - MENU_BUTTON_WIDTH / 2, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, 0);
        this.gameMode = gameMode;
        this.label = label;
    }

    @Override
    public void update() {
        // No columnIndex needed - using single empty button image
    }

    @Override
    public void render(Graphics g) {
        // Draw button background
        g.drawImage(buttonImage, x, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, null);
        
        // Apply visual effects based on state
        if (mousePressed) {
            // Darken when pressed
            g.setColor(new Color(0, 0, 0, 80));
            g.fillRect(x, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        } else if (mouseOver) {
            // Brighten when hover
            g.setColor(new Color(255, 255, 255, 50));
            g.fillRect(x, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        }
        
        // Draw label text centered on button
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(label);
        int textX = x + (MENU_BUTTON_WIDTH - textWidth) / 2;
        int textY = y + (MENU_BUTTON_HEIGHT + fm.getAscent()) / 2 - 4;
        
        // Draw text shadow for better readability
        g.setColor(Color.BLACK);
        g.drawString(label, textX + 2, textY + 2);
        
        // Draw text
        g.setColor(Color.WHITE);
        g.drawString(label, textX, textY);
    }

    @Override
    protected void loadImage() {
        buttonImage = ImageLoaderUtil.loadImage(BUTTON_EMPTY);
        if (buttonImage == null) {
            log.error("Failed to load button_empty.png!");
        }
    }
}
