package io.nghlong3004.view.button;

import io.nghlong3004.model.TileMode;
import io.nghlong3004.util.ImageLoaderUtil;
import io.nghlong3004.view.GameObject;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.GameConstant.SCALE;

public class MapButton implements GameObject {

    private final int x, y, width, height;
    private final TileMode tileMode;
    private BufferedImage mapPreview;
    private boolean mouseOver;
    private boolean mousePressed;

    private Rectangle2D.Float bounds;

    public MapButton(int x, int y, int width, int height, TileMode tileMode) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.tileMode = tileMode;
        this.bounds = new Rectangle2D.Float(x - width / 2f, y - height / 2f, width, height);
        loadMapPreview();
    }

    private void loadMapPreview() {
        // Load a sample tile from the map as preview
        String path = String.format("/images/map/%s/grass.png", tileMode.name);
        try {
            mapPreview = ImageLoaderUtil.loadImage(path);
        } catch (Exception e) {
            // If grass.png doesn't exist, try other common tiles
            try {
                path = String.format("/images/map/%s/wall.png", tileMode.name);
                mapPreview = ImageLoaderUtil.loadImage(path);
            } catch (Exception ex) {
                // Use a default color if no preview available
                mapPreview = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = mapPreview.createGraphics();
                g.setColor(new Color(100, 150, 100));
                g.fillRect(0, 0, 100, 100);
                g.dispose();
            }
        }
    }

    public TileMode getTileMode() {
        return tileMode;
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

        // Draw map preview
        if (mapPreview != null) {
            int previewSize = (int) (height * 0.6f);
            int previewX = boxX + (width - previewSize) / 2;
            int previewY = boxY + 10;
            g2d.drawImage(mapPreview, previewX, previewY, previewSize, previewSize, null);
        }

        // Draw map name
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, (int) (14 * SCALE)));
        String displayName = getDisplayName();
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(displayName);
        int textX = boxX + (width - textWidth) / 2;
        int textY = boxY + height - 10;
        g2d.drawString(displayName, textX, textY);
    }

    private String getDisplayName() {
        return switch (tileMode) {
            case DESERT_MODE -> "DESERT";
            case LAND_MODE -> "LAND";
            case TOWN_MODE -> "TOWN";
            case UNDERWATER_MODE -> "UNDERWATER";
            case XMAS_MODE -> "XMAS";
            default -> tileMode.name;
        };
    }
}
