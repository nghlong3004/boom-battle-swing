package io.nghlong3004.util;

import io.nghlong3004.model.SpawnTile;
import io.nghlong3004.model.TileMap;
import lombok.extern.slf4j.Slf4j;

import java.awt.geom.Rectangle2D;

import static io.nghlong3004.constant.GameConstant.TILES_SIZE;

@Slf4j
public class CollisionUtil {

    private static final int STONE = 0;
    private static final int FLOOR = 1;
    private static final int BRICK = 2;
    private static final int GIFT_BOX = 3;
    private static final int PLAYER_SPAWN = 6;
    private static final int SOLDIER_SPAWN_1 = 7;
    private static final int SOLDIER_SPAWN_2 = 8;
    private static final int SOLDIER_SPAWN_3 = 9;

    public static boolean canMove(Rectangle2D.Float hitbox, float newX, float newY, TileMap tileMap) {
        Rectangle2D.Float newHitbox = new Rectangle2D.Float(newX, newY, hitbox.width, hitbox.height);

        return isNotSolid(newHitbox.x, newHitbox.y, tileMap) && isNotSolid(newHitbox.x + newHitbox.width, newHitbox.y,
                                                                           tileMap) && isNotSolid(newHitbox.x,
                                                                                                  newHitbox.y + newHitbox.height,
                                                                                                  tileMap) && isNotSolid(
                newHitbox.x + newHitbox.width, newHitbox.y + newHitbox.height, tileMap);
    }

    public static boolean isNotSolid(float x, float y, TileMap tileMap) {
        if (tileMap.getData() == null) {
            return true;
        }

        int xOffset = tileMap.getXDrawOffSet();
        int yOffset = tileMap.getYDrawOffSet();

        float relativeX = x - xOffset;
        float relativeY = y - yOffset;

        int gridCol = (int) (relativeX / TILES_SIZE);
        int gridRow = (int) (relativeY / TILES_SIZE);

        if (gridRow < 0 || gridRow >= tileMap.getData().length || gridCol < 0 || gridCol >= tileMap.getData()[0].length) {
            return false;
        }

        int tileValue = tileMap.getData()[gridRow][gridCol];

        return tileValue != STONE && tileValue != BRICK && tileValue != GIFT_BOX;
    }

    public static boolean isDestructible(int gridRow, int gridCol, TileMap tileMap) {
        if (tileMap.getData() == null) {
            return false;
        }

        if (gridRow < 0 || gridRow >= tileMap.getData().length || gridCol < 0 || gridCol >= tileMap.getData()[0].length) {
            return false;
        }

        int tileValue = tileMap.getData()[gridRow][gridCol];

        return tileValue == BRICK || tileValue == GIFT_BOX;
    }

    public static boolean blocksExplosion(int gridRow, int gridCol, TileMap tileMap) {
        if (tileMap.getData() == null) {
            return false;
        }

        if (isGrid(gridRow, gridCol, tileMap)) {
            return true;
        }

        int tileValue = tileMap.getData()[gridRow][gridCol];

        return tileValue == STONE || tileValue == BRICK || tileValue == GIFT_BOX;
    }

    public static void destroyTile(int gridRow, int gridCol, TileMap tileMap) {
        if (tileMap.getData() == null) {
            return;
        }

        if (isGrid(gridRow, gridCol, tileMap)) {
            return;
        }

        int tileValue = tileMap.getData()[gridRow][gridCol];

        if (tileValue == BRICK || tileValue == GIFT_BOX) {
            log.debug("Destroying tile at row={}, col={}, type={}", gridRow, gridCol,
                      tileValue == BRICK ? "BRICK" : "GIFT_BOX");
            tileMap.getData()[gridRow][gridCol] = FLOOR;
        }
    }

    private static boolean isGrid(int gridRow, int gridCol, TileMap tileMap) {
        return gridRow < 0 || gridRow >= tileMap.getData().length || gridCol < 0 || gridCol >= tileMap.getData()[0].length;
    }

    public static boolean isSpawnTile(int tileValue) {
        return SpawnTile.isSpawnTile(tileValue);
    }
    
    public static boolean isSolid(int gridRow, int gridCol, TileMap tileMap) {
        if (tileMap.getData() == null) {
            return true;
        }

        if (gridRow < 0 || gridRow >= tileMap.getData().length || 
            gridCol < 0 || gridCol >= tileMap.getData()[0].length) {
            return true;
        }

        int tileValue = tileMap.getData()[gridRow][gridCol];
        return tileValue == STONE || tileValue == BRICK || tileValue == GIFT_BOX;
    }
}
