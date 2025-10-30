package io.nghlong3004.boom_battle_swing.constant;

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

    public static int GetSpriteAmount(int enemy_type, int enemy_state) {

        switch (enemy_type) {
            case CRABBY:
                switch (enemy_state) {
                    case IDLE:
                        return 9;
                    case RUNNING:
                        return 6;
                    case ATTACK:
                        return 7;
                    case HIT:
                        return 4;
                    case DEAD:
                        return 5;
                }
        }

        return 0;

    }

}
