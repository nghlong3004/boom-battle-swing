package io.nghlong3004.boom_battle_swing.model;

import io.nghlong3004.boom_battle_swing.util.ImageUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;

import static io.nghlong3004.boom_battle_swing.constant.BomberConstant.*;
import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.IMAGE_BOMBER_HEIGHT;
import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.IMAGE_BOMBER_WIDTH;

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
    private int[][] levelData;
    private BufferedImage[][] animations;

    public Bomber(float x, float y, int width, int height, String name) {
        super(x, y, width, height);
        setHitbox(x, y, (int) (width - X_DRAW_OFF_SET - 20), (int) (height - Y_DRAW_OFF_SET - 10));
        this.animations = ImageUtil.importImageBomber(BOMBER_LENGTH_WIDTH, BOMBER_LENGTH_HEIGHT, name,
                                                      IMAGE_BOMBER_WIDTH, IMAGE_BOMBER_HEIGHT);
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
