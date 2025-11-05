package io.nghlong3004.constant;

import io.nghlong3004.configuration.Configuration;

public class GameConstant {

    public static final float SCALE = Configuration.getInstance()
                                                   .getScale();

    public static final int FPS = Configuration.getInstance()
                                               .getFps();
    public static final int UPS = Configuration.getInstance()
                                               .getUps();
    public static final int MAX_SCREEN_ROW = Configuration.getInstance()
                                                          .getMaxScreenRow();
    public static final int MAX_SCREEN_COLUMN = Configuration.getInstance()
                                                             .getMaxScreenColumn();
    public static final int ORIGINAL_TILE_SIZE = Configuration.getInstance()
                                                              .getOriginalTileSize();

    public static final int TILE_SIZE = (int) (ORIGINAL_TILE_SIZE * SCALE);
    public static final int GAME_WIDTH = TILE_SIZE * MAX_SCREEN_COLUMN;
    public static final int GAME_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;
}
