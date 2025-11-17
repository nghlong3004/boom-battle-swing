package io.nghlong3004.game.manager;

import io.nghlong3004.constant.GameConstant;
import io.nghlong3004.loader.MapLoader;
import io.nghlong3004.model.MapData;
import io.nghlong3004.model.type.MapType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MapManager {
    @Getter
    private final MapData map;
    @Getter
    @Setter
    private List<Point> bomberSpawns;

    public MapManager() {
        map = new MapData();
        bomberSpawns = new ArrayList<>();
    }

    public void loadMap(MapType mapType) {
        map.setType(mapType);
        map.setData(MapLoader.loadMapFromFilePath(map.getType()
                                                     .getAssetKey()));
        var data = map.getData();
        bomberSpawns.clear();
        for (int i = 0; i < data.length; ++i) {
            for (int j = 0; j < data[i].length; ++j) {
                if (data[i][j] == 6) {
                    bomberSpawns.add(new Point(i, j));
                }
            }
        }
    }

    public List<Point> getSpawns() {
        var points = new ArrayList<Point>();
        var data = map.getData();
        int spaceY = GameConstant.MAX_SCREEN_COLUMN - data[0].length >>> 1;
        int spaceX = GameConstant.MAX_SCREEN_ROW - data.length >>> 1;
        for (var bomberSpawn : bomberSpawns) {
            int x = bomberSpawn.x + spaceX;
            int y = bomberSpawn.y + spaceY;
            points.add(new Point(GameConstant.TILE_SIZE * y, GameConstant.TILE_SIZE * x));
        }
        return points;
    }
}
