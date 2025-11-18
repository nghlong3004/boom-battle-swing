package io.nghlong3004.game.component.button;

import io.nghlong3004.loader.AudioLoader;
import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.model.type.GameStateType;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.ButtonConstant.MENU_BUTTON_HEIGHT;
import static io.nghlong3004.constant.ButtonConstant.MENU_BUTTON_WIDTH;
import static io.nghlong3004.constant.GameConstant.SCALE;
import static io.nghlong3004.constant.ImageConstant.BUTTON;
import static io.nghlong3004.constant.ImageConstant.BUTTON_TOUCH;

public class TextMenuButton extends GameButton {
    @Getter
    private final GameStateType state;
    private BufferedImage buttonImage;
    private BufferedImage buttonTouchImage;
    private final String text;

    public TextMenuButton(int x, int y, String text, GameStateType state, AudioLoader audioLoader) {
        super(x, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, 0, audioLoader);
        this.text = text;
        this.state = state;
        loadImage();
    }

    @Override
    protected void loadImage() {
        buttonImage = ImageLoader.loadImage(BUTTON);
        buttonTouchImage = ImageLoader.loadImage(BUTTON_TOUCH);
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        BufferedImage currentImage = (mouseOver || mousePressed) ? buttonTouchImage : buttonImage;

        if (currentImage != null) {
            g.drawImage(currentImage, x, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, null);
        }

        int fontSize = (int) (24 * SCALE);
        Font font = new Font("Arial", Font.BOLD, fontSize);
        g2d.setFont(font);

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int textX = x + (MENU_BUTTON_WIDTH - textWidth) / 2;
        int textY = y + (MENU_BUTTON_HEIGHT - textHeight) / 2 + fm.getAscent();

        if (mousePressed) {
            textY += 2;
        }

        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.drawString(text, textX + 2, textY + 2);

        if (mouseOver || mousePressed) {
            g2d.setColor(new Color(255, 255, 100));
        }
        else {
            g2d.setColor(Color.WHITE);
        }
        g2d.drawString(text, textX, textY);
    }
}
