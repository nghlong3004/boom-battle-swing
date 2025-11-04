package io.nghlong3004.map;

import io.nghlong3004.loader.MapLoader;
import io.nghlong3004.type.MapType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapManager {
    private MapData map;


    public void loadMap(MapType type) {
        map.setData(MapLoader.loadMapFromFilePath(type.getAssetKey()));

    }

}
