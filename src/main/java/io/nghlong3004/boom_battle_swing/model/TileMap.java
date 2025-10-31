package io.nghlong3004.boom_battle_swing.model;

import io.nghlong3004.boom_battle_swing.util.FileLoaderUtil;
import lombok.Getter;
import lombok.Setter;

import static io.nghlong3004.boom_battle_swing.constant.GameConstant.*;

@Getter
@Setter
public class TileMap {
    private TileMode mode;
    private int[][] datas;

    private int xDrawOffSet;
    private int yDrawOffSet;

    public void importDataMap(String name) {
        datas = FileLoaderUtil.loadFileMapData(name);
        xDrawOffSet = (GAME_WIDTH - TILES_SIZE * datas[0].length) >>> 1;
        yDrawOffSet = (GAME_HEIGHT - TILES_SIZE * datas.length) >>> 1;
    }
}
