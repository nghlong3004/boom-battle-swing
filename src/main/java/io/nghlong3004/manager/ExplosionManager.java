package io.nghlong3004.manager;

import io.nghlong3004.assets.ObjectAssets;
import io.nghlong3004.entity.Bomber;
import io.nghlong3004.entity.Explosion;
import io.nghlong3004.type.TileType;
import io.nghlong3004.util.AudioHelper;
import io.nghlong3004.util.CollisionUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.constant.GameConstant.TILE_SIZE;

@Slf4j
@RequiredArgsConstructor
public class ExplosionManager {
    @Getter
    private final List<Explosion> explosions = new ArrayList<>();
    private final MapManager mapManager;
    private final BomberManager bomberManager;
    private final AgentManager agentManager;
    private final ItemManager itemManager;

    public void createExplosion(int gridX, int gridY, int range) {
        log.debug("Creating explosion at grid ({}, {}) with range {}", gridX, gridY, range);

        explosions.add(new Explosion(gridX, gridY));
        AudioHelper.playBombExplosionSound();

        createExplosionLine(gridX, gridY, 0, -1, range);
        createExplosionLine(gridX, gridY, 0, 1, range);
        createExplosionLine(gridX, gridY, -1, 0, range);
        createExplosionLine(gridX, gridY, 1, 0, range);
    }

    private void createExplosionLine(int startGridX, int startGridY, int deltaX, int deltaY, int range) {
        int[][] mapData = mapManager.getMap()
                                    .getData();
        int offsetX = CollisionUtil.calculateMapOffsetX(mapData[0].length);
        int offsetY = CollisionUtil.calculateMapOffsetY(mapData.length);

        for (int i = 1; i <= range; i++) {
            int newGridX = startGridX + (deltaY * i);
            int newGridY = startGridY + (deltaX * i);

            int mapRow = newGridX - offsetY;
            int mapCol = newGridY - offsetX;

            if (mapRow < 0 || mapCol < 0 || mapRow >= mapData.length || mapCol >= mapData[0].length) {
                log.debug("Explosion line stopped: out of bounds at grid ({}, {})", newGridX, newGridY);
                break;
            }

            int tileType = mapData[mapRow][mapCol];

            if (tileType == TileType.STONE.id) {
                log.debug("Explosion line stopped: hit STONE at grid ({}, {})", newGridX, newGridY);
                break;
            }

            explosions.add(new Explosion(newGridX, newGridY));
            log.debug("Explosion created at grid ({}, {}) - map position [{}, {}] = {}", newGridX, newGridY, mapRow,
                      mapCol, tileType);

            if (itemManager != null) {
                itemManager.destroyItemAt(newGridX, newGridY);
            }

            if (tileType == TileType.BRICK.id || tileType == TileType.GIFT_BOX.id) {
                if (tileType == TileType.GIFT_BOX.id && itemManager != null) {
                    itemManager.spawnItem(newGridX, newGridY);
                }

                mapData[mapRow][mapCol] = TileType.FLOOR.id;
                log.debug("Tile destroyed at grid ({}, {}) - map position [{}, {}] - type: {}", newGridX, newGridY,
                          mapRow, mapCol, tileType == TileType.BRICK.id ? "BRICK" : "GIFT_BOX");
                break;
            }
        }
    }

    public void update() {
        checkBomberDamage(bomberManager.getBombers());
        checkBomberDamage(agentManager.getAgents());

        for (Explosion explosion : explosions) {
            explosion.update();
        }
        explosions.removeIf(Explosion::shouldBeRemoved);
    }

    private void checkBomberDamage(List<Bomber> bombers) {
        if (explosions.isEmpty()) {
            return;
        }

        for (Explosion explosion : explosions) {
            int explosionPixelX = explosion.getPixelX();
            int explosionPixelY = explosion.getPixelY();

            for (var bomber : bombers) {
                float bomberX = bomber.getBox().x;
                float bomberY = bomber.getBox().y;
                float bomberWidth = bomber.getBox().width;
                float bomberHeight = bomber.getBox().height;

                if (bomberX < explosionPixelX + TILE_SIZE && bomberX + bomberWidth > explosionPixelX && bomberY < explosionPixelY + TILE_SIZE && bomberY + bomberHeight > explosionPixelY) {

                    if (!bomber.isDying() && bomber.isAlive()) {
                        bomber.startDying();
                        log.info("Bomber at ({}, {}) killed by explosion at grid ({}, {})", bomberX, bomberY,
                                 explosion.getGridX(), explosion.getGridY());
                    }
                }
            }
        }
    }

    public void render(Graphics g) {
        BufferedImage[] animationFrames = ObjectAssets.getInstance()
                                                      .getExplosionAnimationFrameAssets();
        if (animationFrames == null || animationFrames.length == 0) {
            return;
        }

        List<Explosion> explosionsCopy = new ArrayList<>(explosions);
        for (Explosion explosion : explosionsCopy) {
            int frameIndex = Math.min(explosion.getAnimationFrame(), animationFrames.length - 1);
            BufferedImage currentFrame = animationFrames[frameIndex];

            if (currentFrame != null) {
                g.drawImage(currentFrame, explosion.getPixelX(), explosion.getPixelY(), TILE_SIZE, TILE_SIZE, null);
            }
        }
        itemManager.render(g);
    }

    public void reset() {
        explosions.clear();
    }
}
