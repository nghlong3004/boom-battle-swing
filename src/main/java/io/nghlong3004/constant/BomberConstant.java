package io.nghlong3004.constant;

public final class BomberConstant extends GameConstant {

    public static final int IDLE = 0;
    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;

    public static final int CRABBY = 0;

    public static final int RUNNING = 1;
    public static final int ATTACK = 2;
    public static final int HIT = 3;
    public static final int DEAD = 4;

    public static final int CRABBY_WIDTH_DEFAULT = 72;
    public static final int CRABBY_HEIGHT_DEFAULT = 32;

    public static final int CRABBY_WIDTH = (int) (CRABBY_WIDTH_DEFAULT * SCALE);
    public static final int CRABBY_HEIGHT = (int) (CRABBY_HEIGHT_DEFAULT * SCALE);

    public static final int BOMBER_WIDTH_DEFAULT = 50;
    public static final int BOMBER_HEIGHT_DEFAULT = 30;

    public static final int BOMBER_WIDTH = (int) (BOMBER_WIDTH_DEFAULT * SCALE);
    public static final int BOMBER_HEIGHT = (int) (BOMBER_HEIGHT_DEFAULT * SCALE);

    public static final int ANIMATION_SPEED = 15;

    public static final float BOMBER_SPEED = 1f * SCALE;

    public static final float X_DRAW_OFF_SET = 5f * SCALE;
    public static final float Y_DRAW_OFF_SET = 1f * SCALE;

    public static final int BOMBER_LENGTH_WIDTH = 4;
    public static final int BOMBER_LENGTH_HEIGHT = 5;

    public static final float DIAGONAL_SPEED_MODIFIER = 0.70710678f;

}
