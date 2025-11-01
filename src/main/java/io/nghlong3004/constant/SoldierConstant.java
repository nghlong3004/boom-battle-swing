package io.nghlong3004.constant;

public final class SoldierConstant extends GameConstant {
    
    public static final int SOLDIER_WIDTH_DEFAULT = 48;
    public static final int SOLDIER_HEIGHT_DEFAULT = 48;
    
    public static final int SOLDIER_WIDTH = (int) (SOLDIER_WIDTH_DEFAULT * SCALE);
    public static final int SOLDIER_HEIGHT = (int) (SOLDIER_HEIGHT_DEFAULT * SCALE);
    
    public static final float SOLDIER_SPEED = 0.5f * SCALE;
    
    public static final int DIRECTION_CHANGE_COOLDOWN = 120;
    public static final int AI_UPDATE_INTERVAL = 5;
    
    private SoldierConstant() {
    }
}
