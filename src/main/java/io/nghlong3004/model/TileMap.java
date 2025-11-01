package io.nghlong3004.model;

import io.nghlong3004.util.FileLoaderUtil;
import lombok.Getter;
import lombok.Setter;

import static io.nghlong3004.constant.GameConstant.*;

@Getter
@Setter
public class TileMap {
    private TileMode mode;
    private int[][] data;

    private int xDrawOffSet;
    private int yDrawOffSet;

    public void importDataMap(String name) {
        data = FileLoaderUtil.loadFileMapData(name);
        xDrawOffSet = (GAME_WIDTH - TILES_SIZE * data[0].length) >>> 1;
        yDrawOffSet = (GAME_HEIGHT - TILES_SIZE * data.length) >>> 1;
    }
}
