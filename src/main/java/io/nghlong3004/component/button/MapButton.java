package io.nghlong3004.component.button;

import io.nghlong3004.entity.MapType;
import io.nghlong3004.loader.ImageLoader;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.GameConstant.SCALE;

public class MapButton extends GameButton {
    @Getter
    private final MapType tileMode;
    protected BufferedImage image;

    public MapButton(int x, int y, int width, int height, MapType tileMode) {
        super((int) (x - width / 2f), (int) (y - height / 2f), width, height);
        this.tileMode = tileMode;
        loadImage();
    }

    @Override
    protected void loadImage() {

        String path = String.format("/images/map/%s/grass.png", tileMode.getAssetKey());
        try {
            image = ImageLoader.loadImage(path);
        } catch (Exception e) {

            try {
                path = String.format("/images/map/%s/wall.png", tileMode.getAssetKey());
                image = ImageLoader.loadImage(path);
            } catch (Exception ex) {

                image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                g.setColor(new Color(100, 150, 100));
                g.fillRect(0, 0, 100, 100);
                g.dispose();
            }
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        render(g, getDisplayName());
    }

    public void render(Graphics g, String name) {
        Graphics2D g2d = (Graphics2D) g;

        int boxX = (int) box.x;
        int boxY = (int) box.y;

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

    private String getDisplayName() {
        return switch (tileMode) {
            case DESERT_MODE -> "DESERT";
            case LAND_MODE -> "LAND";
            case TOWN_MODE -> "TOWN";
            case UNDERWATER_MODE -> "UNDERWATER";
            case XMAS_MODE -> "XMAS";
            default -> tileMode.getAssetKey();
        };
    }
}
