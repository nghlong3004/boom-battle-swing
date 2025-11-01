package io.nghlong3004.system;

import io.nghlong3004.constant.MapConstant;
import io.nghlong3004.model.TileMap;
import io.nghlong3004.util.ObjectContainer;

import java.awt.*;

import static io.nghlong3004.constant.GameConstant.TILES_SIZE;
import static io.nghlong3004.model.Tile.FLOOR;


public class TileMapRenderSystem implements RenderSystem {
    @Override
    public void render(Graphics g, Object entity) {
        TileMap tileMap = (TileMap) entity;
        if (tileMap.getMode() != GameStateContextHolder.MODE) {
            tileMap.setMode(GameStateContextHolder.MODE);
            String name = MapConstant.FILE_PATH_TEMPLATE.formatted(tileMap.getMode().name);
            tileMap.importDataMap(name);
        }
        draw(g, tileMap);
    }

    private void draw(Graphics g, TileMap tileMap) {
        int row = tileMap.getMode().index;
        var datas = tileMap.getData();
        var xDrawOffSet = tileMap.getXDrawOffSet();
        var yDrawOffSet = tileMap.getYDrawOffSet();
        var images = ObjectContainer.getImageContainer().getTileSprites();
        for (int j = 0; j < datas.length; j++) {
            for (int i = 0; i < datas[j].length; i++) {
                int column = datas[j][i];
                g.drawImage(images[row][FLOOR.index], TILES_SIZE * i + xDrawOffSet, TILES_SIZE * j + yDrawOffSet,
                            TILES_SIZE, TILES_SIZE, null);
                if (column != FLOOR.index) {
                    g.drawImage(images[row][column], TILES_SIZE * i + xDrawOffSet, TILES_SIZE * j + yDrawOffSet,
                                TILES_SIZE, TILES_SIZE, null);
                }
            }
        }
    }
}
