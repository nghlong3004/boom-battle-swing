package io.nghlong3004.boom_battle_swing.constant;

public class GameConstant {
    public static final int TILE_DEFAULT_SIZE = 32;
    public static final int TILES_IN_WIDTH = 26;
    public static final int TILES_IN_HEIGHT = 14;

    public static final float SCALE = 1.5f;

    public static final int TILES_SIZE = (int) (TILE_DEFAULT_SIZE * SCALE);
    public static final int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public static final int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public static final float SPEED_PLAYER = 1f * SCALE;

    public static final float HITBOX_WIDTH_OFF_SET = 40f * SCALE;
    public static final float HITBOX_HEIGHT_OFF_SET = 12f * SCALE;

    public static final long MOVE_SFX_COOLDOWN_MS = 120;
}
