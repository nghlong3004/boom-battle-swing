package io.nghlong3004.ai;

import io.nghlong3004.model.Bomb;
import io.nghlong3004.model.Explosion;
import io.nghlong3004.model.TileMap;
import io.nghlong3004.util.CollisionUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
@Getter
public class DangerMap {
    private final boolean[][] dangerous;
    private final int rows;
    private final int cols;
    private int dangerousTileCount = 0;

    public DangerMap(TileMap tileMap, List<Bomb> bombs, List<Explosion> explosions) {
        this.rows = tileMap.getData().length;
        this.cols = tileMap.getData()[0].length;
        this.dangerous = new boolean[rows][cols];


        for (Explosion explosion : explosions) {
            for (var tile : explosion.getExplosionTiles()) {
                markDangerous(tile.getGridRow(), tile.getGridCol());
            }
        }


        for (Bomb bomb : bombs) {
            markBombDanger(bomb, tileMap);
        }

        log.trace("DangerMap created: {} bombs, {} explosions, {} dangerous tiles", bombs.size(), explosions.size(),
                  dangerousTileCount);
    }

    private void markBombDanger(Bomb bomb, TileMap tileMap) {
        int bombRow = bomb.getGridRow();
        int bombCol = bomb.getGridCol();
        int range = bomb.getExplosionRange();


        markDangerous(bombRow, bombCol);


        markExplosionDirection(bombRow, bombCol, -1, 0, range, tileMap);
        markExplosionDirection(bombRow, bombCol, 1, 0, range, tileMap);
        markExplosionDirection(bombRow, bombCol, 0, -1, range, tileMap);
        markExplosionDirection(bombRow, bombCol, 0, 1, range, tileMap);
    }

    private void markExplosionDirection(int startRow, int startCol, int rowDelta, int colDelta, int range,
                                        TileMap tileMap) {
        for (int i = 1; i <= range; i++) {
            int row = startRow + (i * rowDelta);
            int col = startCol + (i * colDelta);

            if (!isInBounds(row, col)) {
                break;
            }

            markDangerous(row, col);


            if (CollisionUtil.blocksExplosion(row, col, tileMap)) {
                break;
            }
        }
    }

    private void markDangerous(int row, int col) {
        if (isInBounds(row, col) && !dangerous[row][col]) {
            dangerous[row][col] = true;
            dangerousTileCount++;
        }
    }

    public boolean isDangerous(int row, int col) {
        return isInBounds(row, col) && dangerous[row][col];
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
}
