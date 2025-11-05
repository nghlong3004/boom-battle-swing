package io.nghlong3004.manager;

import io.nghlong3004.constant.GameConstant;
import io.nghlong3004.loader.MapLoader;
import io.nghlong3004.map.MapData;
import io.nghlong3004.type.MapType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MapManager {
    @Getter
    private final MapData map;

    public MapManager() {
        map = new MapData();
    }

    public void loadMap() {
        map.setData(MapLoader.loadMapFromFilePath(map.getType()
                                                     .getAssetKey()));
    }

    public void setType(MapType type) {
        map.setType(type);
    }

    public List<Point> getSpawns(int id) {
        var points = new ArrayList<Point>();
        var data = map.getData();
        int spaceY = GameConstant.MAX_SCREEN_COLUMN - data[0].length >>> 1;
        int spaceX = GameConstant.MAX_SCREEN_ROW - data.length >>> 1;
        for (int i = 0; i < data.length; ++i) {
            for (int j = 0; j < data[i].length; ++j) {
                if (data[i][j] == id) {
                    int x = i + spaceX;
                    int y = j + spaceY;
                    points.add(new Point(GameConstant.TILE_SIZE * y, GameConstant.TILE_SIZE * x));
                }
            }
        }
        return points;
    }
}
