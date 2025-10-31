package io.nghlong3004.boom_battle_swing.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static io.nghlong3004.boom_battle_swing.constant.BomberConstant.*;

@Getter
@Setter
@Slf4j
public class Bomber extends Entity {

    private int animationTick, animationIndex;
    private final int animationSpeed = ANIMATION_SPEED;
    private boolean moving = false;
    private int direction = DOWN;

    private boolean up, down, right, left;

    private float bomberSpeed = BOMBER_SPEED;

    public Bomber(float x, float y, int width, int height) {
        super(x, y, width, height);
        setHitbox(x, y, (int) (width - X_DRAW_OFF_SET - 20), (int) (height - Y_DRAW_OFF_SET - 10));
    }

    public void resetDirection() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void resetAll() {
        hitbox.x = x;
        hitbox.y = y;
        resetDirection();
        animationTick = 0;
        animationIndex = 0;
        moving = false;
        direction = DOWN;
    }
}
