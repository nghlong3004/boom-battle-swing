package io.nghlong3004.util;

import io.nghlong3004.entity.Bomb;
import io.nghlong3004.entity.Bomber;
import io.nghlong3004.manager.BombManager;
import io.nghlong3004.manager.BomberManager;
import io.nghlong3004.manager.MapManager;
import io.nghlong3004.type.TileType;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.geom.Rectangle2D;
import java.util.List;

import static io.nghlong3004.constant.GameConstant.TILE_SIZE;

@Slf4j
public class MapCollisionChecker implements CollisionChecker {
    private final MapManager mapManager;
    @Setter
    private BomberManager bomberManager;
    @Setter
    private BombManager bombManager;

    public MapCollisionChecker(MapManager mapManager) {
        this.mapManager = mapManager;
    }

    @Override
    public boolean canMoveTo(Rectangle2D.Float newHitbox, Bomber currentBomber) {
        if (isCollidingWithSolid(newHitbox.x, newHitbox.y, newHitbox.width, newHitbox.height)) {
            return false;
        }

        if (isCollidingWithBombs(newHitbox)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isCollidingWithSolid(float x, float y, float width, float height) {
        int[][] mapData = mapManager.getMap()
                                    .getData();
        int offsetX = CollisionUtil.calculateMapOffsetX(mapData[0].length);
        int offsetY = CollisionUtil.calculateMapOffsetY(mapData.length);

        int topRow = CollisionUtil.pixelToGridRow(y, offsetY);
        int bottomRow = CollisionUtil.pixelToGridRow(y + height - 1, offsetY);
        int leftCol = CollisionUtil.pixelToGridColumn(x, offsetX);
        int rightCol = CollisionUtil.pixelToGridColumn(x + width - 1, offsetX);

        topRow = Math.max(0, Math.min(topRow, mapData.length - 1));
        bottomRow = Math.max(0, Math.min(bottomRow, mapData.length - 1));
        leftCol = Math.max(0, Math.min(leftCol, mapData[0].length - 1));
        rightCol = Math.max(0, Math.min(rightCol, mapData[0].length - 1));

        return isSolidTile(mapData[topRow][leftCol]) || isSolidTile(mapData[topRow][rightCol]) || isSolidTile(
                mapData[bottomRow][leftCol]) || isSolidTile(mapData[bottomRow][rightCol]);
    }

    private boolean isSolidTile(int tileId) {
        return tileId == TileType.STONE.id || tileId == TileType.BRICK.id || tileId == TileType.GIFT_BOX.id;
    }

    private boolean isCollidingWithBombs(Rectangle2D.Float hitbox) {
        if (bombManager == null) {
            return false;
        }

        List<Bomb> bombs = bombManager.getBombs();
        for (Bomb bomb : bombs) {
            if (bomb.isExploded() || !bomb.isSolid()) {
                continue;
            }

            Rectangle2D.Float bombHitbox = new Rectangle2D.Float(bomb.getPixelX(), bomb.getPixelY(), TILE_SIZE,
                                                                 TILE_SIZE);

            if (hitbox.intersects(bombHitbox)) {
                return true;
            }
        }

        return false;
    }

    private boolean isCollidingWithOtherBombers(Rectangle2D.Float hitbox, Bomber currentBomber) {
        if (bomberManager == null) {
            return false;
        }

        List<Bomber> bombers = bomberManager.getBombers();
        for (Bomber bomber : bombers) {
            if (bomber == currentBomber || !bomber.isAlive()) {
                continue;
            }

            if (hitbox.intersects(bomber.getBox())) {
                return true;
            }
        }

        return false;
    }
}
