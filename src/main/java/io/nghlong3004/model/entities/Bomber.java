package io.nghlong3004.model.entities;

import io.nghlong3004.model.type.SkinType;
import lombok.Getter;
import lombok.Setter;

import static io.nghlong3004.constant.BomberConstant.BOMBER_HEIGHT;
import static io.nghlong3004.constant.BomberConstant.BOMBER_WIDTH;

@Getter
@Setter
public class Bomber extends Entity {

    private int maxBombs = 1;
    private int currentBombs = 0;
    private int explosionRange = 1;
    private float speedBoost = 0f;
    private boolean placeBombRequested = false;

    private boolean dying = false;
    private int deathAnimationTick = 0;
    private int deathAnimationFrame = 0;
    private static final int DEATH_ANIMATION_SPEED = 10;
    private static final int DEATH_ANIMATION_FRAMES = 8;

    public Bomber(float x, float y, SkinType skinType) {
        super(x, y, BOMBER_WIDTH, BOMBER_HEIGHT, skinType);
    }

    @Override
    protected float getDefaultSpeed() {
        return 1;
    }

    public void setHasSpeedBoost(boolean b) {

    }


    public void updateDeathAnimation() {
        if (!dying) {
            return;
        }

        deathAnimationTick++;
        if (deathAnimationTick >= DEATH_ANIMATION_SPEED) {
            deathAnimationTick = 0;
            deathAnimationFrame++;

            if (deathAnimationFrame >= DEATH_ANIMATION_FRAMES) {
                setAlive(false);
            }
        }
    }


    public void startDying() {
        if (!dying) {
            dying = true;
            deathAnimationTick = 0;
            deathAnimationFrame = 0;
        }
    }
}
