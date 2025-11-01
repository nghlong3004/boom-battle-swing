package io.nghlong3004.view.button;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.GameConstant.SCALE;

@Getter
@Setter
public abstract class SelectionButton {

    protected int width;
    protected int height;
    protected BufferedImage image;
    protected boolean mouseOver;
    protected boolean mousePressed;

    protected final Rectangle2D.Float bounds;

    protected SelectionButton(int x, int y, int width, int height) {
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle2D.Float(x - width / 2f, y - height / 2f, width, height);
    }

    protected abstract void loadImage();

    public boolean isMouseOver(MouseEvent e) {
        return bounds.contains(e.getX(), e.getY());
    }

    public void render(Graphics g, String name) {
        Graphics2D g2d = (Graphics2D) g;

        int boxX = (int) bounds.x;
        int boxY = (int) bounds.y;

        if (mousePressed) {
            g2d.setColor(new Color(100, 100, 100, 200));
        }
        else if (mouseOver) {
            g2d.setColor(new Color(255, 255, 255, 150));
        }
        else {
            g2d.setColor(new Color(50, 50, 50, 180));
        }

        g2d.fillRoundRect(boxX, boxY, width, height, 20, 20);

        g2d.setColor(mouseOver ? Color.YELLOW : Color.WHITE);
        g2d.setStroke(new BasicStroke(mouseOver ? 4 : 2));
        g2d.drawRoundRect(boxX, boxY, width, height, 20, 20);

        if (image != null) {
            int avatarSize = (int) (height * 0.7f);
            int avatarX = boxX + (width - avatarSize) / 2;
            int avatarY = boxY + 10;
            g2d.drawImage(image, avatarX, avatarY, avatarSize, avatarSize, null);
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, (int) (16 * SCALE)));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(name);
        int textX = boxX + (width - textWidth) / 2;
        int textY = boxY + height - 15;
        g2d.drawString(name, textX, textY);
    }

}
