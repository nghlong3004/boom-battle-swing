package io.nghlong3004.constant;

import static io.nghlong3004.constant.GameConstant.SCALE;
import static io.nghlong3004.constant.GameConstant.TILE_SIZE;

public class EntityConstant {
    public static final int IDLE = 0;
    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;

    public static final float DIAGONAL_SPEED_MODIFIER = 0.70710678f;
    public static final float X_DRAW_OFF_SET = 5f * SCALE;
    public static final float Y_DRAW_OFF_SET = 2f * SCALE;
    public static final int ENTITY_WIDTH = (int) ((TILE_SIZE * SCALE) - X_DRAW_OFF_SET);
    public static final int ENTITY_HEIGHT = (int) ((TILE_SIZE * SCALE) - Y_DRAW_OFF_SET);

}
