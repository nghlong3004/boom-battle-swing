package io.nghlong3004.manager;

import io.nghlong3004.assets.ObjectAssets;
import io.nghlong3004.entity.Bomb;
import io.nghlong3004.entity.Bomber;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.constant.GameConstant.SCALE;
import static io.nghlong3004.constant.GameConstant.TILE_SIZE;

@Slf4j
public class BombManager {
    @Getter
    private final List<Bomb> bombs;
    private final MapManager mapManager;

    public BombManager(MapManager mapManager) {
        this.bombs = new ArrayList<>();
        this.mapManager = mapManager;
    }

    public void placeBomb(Bomber bomber) {
        if (bomber.getCurrentBombs() >= bomber.getMaxBombs()) {
            return;
        }

        // Get bomber's center position
        float bomberCenterX = bomber.getBox().x;
        float bomberCenterY = bomber.getBox().y;

        // Calculate which grid tile the bomber is on
        int gridX = (int) (bomberCenterY / TILE_SIZE);
        int gridY = (int) (bomberCenterX / TILE_SIZE);

        // Check if there's already a bomb at this grid position
        for (Bomb existingBomb : bombs) {
            if (existingBomb.getGridX() == gridX && existingBomb.getGridY() == gridY) {
                return;
            }
        }

        // Create bomb at the grid position
        Bomb bomb = new Bomb(gridX, gridY, bomber.getExplosionRange(), bomber);
        bombs.add(bomb);
        bomber.setCurrentBombs(bomber.getCurrentBombs() + 1);
        log.debug("Bomb placed at grid position ({}, {}) by bomber", gridX, gridY);
    }

    public void update() {
        for (Bomb bomb : bombs) {
            bomb.update();
        }

        bombs.removeIf(Bomb::shouldBeRemoved);
    }

    public void render(Graphics g) {
        BufferedImage[] bombSprites = ObjectAssets.getInstance()
                                                  .getBombAssets();

        if (bombSprites == null || bombSprites.length == 0) {
            return;
        }

        for (Bomb bomb : bombs) {
            if (!bomb.isExploded()) {
                int frameIndex = bomb.getAnimationFrame() % bombSprites.length;
                BufferedImage sprite = bombSprites[frameIndex];

                if (sprite != null) {
                    g.drawImage((Image) sprite, bomb.getPixelX() - (int) (TILE_SIZE * SCALE) / 8,
                                bomb.getPixelY() - (int) (TILE_SIZE * SCALE) / 4, (int) (TILE_SIZE * SCALE),
                                (int) (TILE_SIZE * SCALE), null);
                }
            }
        }
    }

    public void reset() {
        bombs.clear();
    }

}
