package io.nghlong3004.view.button;

import io.nghlong3004.model.BomberSkin;
import io.nghlong3004.util.ImageLoaderUtil;
import io.nghlong3004.view.GameObject;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.GameConstant.SCALE;
import static io.nghlong3004.constant.ImageConstant.BOMBER_AVATAR_TEMPLATE;

public class SkinButton implements GameObject {

    private final int x, y, width, height;
    private final BomberSkin skin;
    private BufferedImage skinAvatar;
    private boolean mouseOver;
    private boolean mousePressed;

    private Rectangle2D.Float bounds;

    public SkinButton(int x, int y, int width, int height, BomberSkin skin) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.skin = skin;
        this.bounds = new Rectangle2D.Float(x - width / 2f, y - height / 2f, width, height);
        loadSkinAvatar();
    }

    private void loadSkinAvatar() {
        String path = BOMBER_AVATAR_TEMPLATE.formatted(skin.name);
        skinAvatar = ImageLoaderUtil.loadImage(path);
    }

    public BomberSkin getSkin() {
        return skin;
    }

    public boolean isMouseOver(MouseEvent e) {
        return bounds.contains(e.getX(), e.getY());
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void reset() {
        mouseOver = false;
        mousePressed = false;
    }

    @Override
    public void update() {
        // No animation needed
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw background box
        int boxX = (int) bounds.x;
        int boxY = (int) bounds.y;

        // Apply hover/press effects
        if (mousePressed) {
            g2d.setColor(new Color(100, 100, 100, 200));
        } else if (mouseOver) {
            g2d.setColor(new Color(255, 255, 255, 150));
        } else {
            g2d.setColor(new Color(50, 50, 50, 180));
        }

        g2d.fillRoundRect(boxX, boxY, width, height, 20, 20);

        // Draw border
        g2d.setColor(mouseOver ? Color.YELLOW : Color.WHITE);
        g2d.setStroke(new BasicStroke(mouseOver ? 4 : 2));
        g2d.drawRoundRect(boxX, boxY, width, height, 20, 20);

        // Draw avatar
        if (skinAvatar != null) {
            int avatarSize = (int) (height * 0.7f);
            int avatarX = boxX + (width - avatarSize) / 2;
            int avatarY = boxY + 10;
            g2d.drawImage(skinAvatar, avatarX, avatarY, avatarSize, avatarSize, null);
        }

        // Draw skin name
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, (int) (16 * SCALE)));
        String name = skin.name.toUpperCase();
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(name);
        int textX = boxX + (width - textWidth) / 2;
        int textY = boxY + height - 15;
        g2d.drawString(name, textX, textY);
    }
}
