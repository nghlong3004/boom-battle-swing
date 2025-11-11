package io.nghlong3004.game.manager;

import io.nghlong3004.assets.ObjectAssets;
import io.nghlong3004.model.entities.Bomb;
import io.nghlong3004.model.entities.Bomber;
import io.nghlong3004.util.AudioHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.constant.GameConstant.SCALE;
import static io.nghlong3004.constant.GameConstant.TILE_SIZE;

@Slf4j
public class BombManager {
    @Getter
    private final List<Bomb> bombs;
    @Setter
    private ExplosionManager explosionManager;

    public BombManager() {
        this.bombs = new ArrayList<>();
    }

    public void placeBomb(Bomber bomber) {
        if (bomber.getCurrentBombs() >= bomber.getMaxBombs()) {
            return;
        }

        float bomberCenterX = bomber.getBox().x;
        float bomberCenterY = bomber.getBox().y;

        int gridX = (int) (bomberCenterY / TILE_SIZE);
        int gridY = (int) (bomberCenterX / TILE_SIZE);

        for (Bomb existingBomb : bombs) {
            if (existingBomb.getGridX() == gridX && existingBomb.getGridY() == gridY) {
                return;
            }
        }

        Bomb bomb = new Bomb(gridX, gridY, bomber.getExplosionRange(), bomber);
        bombs.add(bomb);
        bomber.setCurrentBombs(bomber.getCurrentBombs() + 1);
        AudioHelper.playSetBombSound();
        log.debug("Bomb placed at grid position ({}, {}) by bomber", gridX, gridY);
    }

    public void update() {
        for (Bomb bomb : bombs) {
            if (!bomb.isExploded()) {
                bomb.update();

                if (!bomb.isSolid() && bomb.getOwner() != null) {
                    Rectangle2D.Float bombHitbox = new Rectangle2D.Float(bomb.getPixelX(), bomb.getPixelY(), TILE_SIZE,
                                                                         TILE_SIZE);

                    Rectangle2D.Float ownerHitbox = bomb.getOwner()
                                                        .getBox();
                    if (!bombHitbox.intersects(ownerHitbox)) {
                        bomb.setSolid(true);
                        log.debug("Bomb at ({}, {}) became solid", bomb.getGridX(), bomb.getGridY());
                    }
                }

                if (bomb.isExploded() && explosionManager != null) {
                    explosionManager.createExplosion(bomb.getGridX(), bomb.getGridY(), bomb.getExplosionRange());
                }
            }
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
