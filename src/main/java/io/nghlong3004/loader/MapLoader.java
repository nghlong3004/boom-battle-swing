package io.nghlong3004.loader;

import io.nghlong3004.constant.MapConstant;
import io.nghlong3004.type.MapType;
import io.nghlong3004.type.TileType;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.util.Arrays;

@Slf4j
public class MapLoader {
    private static final String SPACE = "\\s+";

    public static int[][] loadMapFromFilePath(String name) {
        String filePath = "/map_data/" + name + ".txt";
        String raw = FileLoader.loadDataFromFilePath(filePath);
        // 0 1 2 3 4 10 11 -> [0, 1, 2, 3, 4, 10, 11] map String to int[][]
        return raw.lines()
                  .map(line -> Arrays.stream(line.trim()
                                                 .split(SPACE))
                                     .mapToInt(Integer::parseInt)
                                     .toArray())
                  .toArray(int[][]::new);
    }

    public static BufferedImage[][] loadMapAssets() {
        log.info("Loading title sprites");
        var data = new BufferedImage[5][4];
        for (MapType row : MapType.values()) {
            for (TileType column : TileType.values()) {
                data[row.id][column.id] = loadTile(row, column);
            }
        }
        return data;
    }

    private static BufferedImage loadTile(MapType row, TileType column) {
        String path = MapConstant.IMAGE_PATH_TEMPLATE.formatted(row.getAssetKey(), column.getAssetKey());
        return ImageLoader.loadImage(path);
    }
}
