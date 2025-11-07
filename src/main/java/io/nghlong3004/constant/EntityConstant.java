package io.nghlong3004.constant;

import static io.nghlong3004.constant.BomberConstant.BOMBER_HEIGHT;
import static io.nghlong3004.constant.BomberConstant.BOMBER_WIDTH;

public class EntityConstant {
    public static final int IDLE = 4;
    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;

    public static final float DIAGONAL_SPEED_MODIFIER = 0.70710678f;

    public static final float HITBOX_WIDTH = BOMBER_WIDTH * 0.5f;
    public static final float HITBOX_HEIGHT = BOMBER_HEIGHT * 0.5f;
    public static final float HITBOX_OFFSET_X = (BOMBER_WIDTH - HITBOX_WIDTH) / 2;
    public static final float HITBOX_OFFSET_Y = (BOMBER_HEIGHT - HITBOX_HEIGHT) / 2;
}
