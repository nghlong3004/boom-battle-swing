package io.nghlong3004.boom_battle_swing.view.render;

import io.nghlong3004.boom_battle_swing.constant.MapConstant;
import io.nghlong3004.boom_battle_swing.model.TileMap;
import io.nghlong3004.boom_battle_swing.model.TileMode;
import io.nghlong3004.boom_battle_swing.util.ObjectContainer;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static io.nghlong3004.boom_battle_swing.constant.GameConstant.TILES_SIZE;
import static io.nghlong3004.boom_battle_swing.model.Tile.FLOOR;

public class TileMapRenderer implements Renderer {

    @Override
    public void render(Graphics g, Object entity) {
        TileMap tileMap = (TileMap) entity;
        if (tileMap.getMode() != TileMode.MODE) {
            tileMap.setMode(TileMode.MODE);
            String name = MapConstant.FILE_PATH_TEMPLATE.formatted(tileMap.getMode().name);
            tileMap.importDataMap(name);
        }
        draw(g, tileMap);
    }

    private void draw(Graphics g, TileMap tileMap) {
        int row = tileMap.getMode().index;
        var datas = tileMap.getDatas();
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

    @Override
    public void drawHitbox(Graphics g, Rectangle2D.Float hitbox) {

    }
}
