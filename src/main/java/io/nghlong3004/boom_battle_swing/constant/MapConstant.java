package io.nghlong3004.boom_battle_swing.constant;

public class MapConstant {
    public static final int FLOOR = 0;

    public static final int SOFT_WALL = 1;

    public static final int SOLID_WALL = 2;

    public static final int TILE_WIDTH_DEFAULT = 16;
    public static final int TILE_HEIGHT_DEFAULT = 16;

    public static final float SCALE = 3.0f;
    public static final int TILE_SIZE = (int) (TILE_WIDTH_DEFAULT * SCALE);

    public static final String IMAGE_PATH_TEMPLATE = "/images/map/%s/%s.png";

    public static final String FILE_PATH_TEMPLATE = "/map_data/%s.txt";

}
