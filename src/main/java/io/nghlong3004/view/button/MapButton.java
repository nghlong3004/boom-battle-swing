package io.nghlong3004.view.button;

import io.nghlong3004.model.TileMode;
import io.nghlong3004.util.ImageLoaderUtil;
import io.nghlong3004.view.GameObject;
import lombok.Getter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MapButton extends SelectionButton implements GameObject {

    @Getter
    private final TileMode tileMode;

    public MapButton(int x, int y, int width, int height, TileMode tileMode) {
        super(x, y, width, height);
        this.tileMode = tileMode;
        loadImage();
    }

    @Override
    protected void loadImage() {

        String path = String.format("/images/map/%s/grass.png", tileMode.name);
        try {
            image = ImageLoaderUtil.loadImage(path);
        } catch (Exception e) {

            try {
                path = String.format("/images/map/%s/wall.png", tileMode.name);
                image = ImageLoaderUtil.loadImage(path);
            } catch (Exception ex) {

                image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                g.setColor(new Color(100, 150, 100));
                g.fillRect(0, 0, 100, 100);
                g.dispose();
            }
        }
    }

    public boolean isMouseOver(MouseEvent e) {
        return bounds.contains(e.getX(), e.getY());
    }

    public void reset() {
        mouseOver = false;
        mousePressed = false;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        render(g, getDisplayName());
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
