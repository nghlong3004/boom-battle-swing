package io.nghlong3004.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveEffect {
    private float x;
    private float y;
    private int animationTick;
    private int animationFrame;
    private int lifeTime;
    private static final int MAX_LIFETIME = 90;
    private static final int ANIMATION_SPEED = 3;

    public MoveEffect(float x, float y) {
        this.x = x;
        this.y = y;
        this.animationTick = 0;
        this.animationFrame = 0;
        this.lifeTime = 0;
    }

    public void update() {
        lifeTime++;
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationFrame = (animationFrame + 1) % 6;
        }
    }

    public boolean shouldRemove() {
        return lifeTime >= MAX_LIFETIME;
    }

    public float getAlpha() {
        return 1.0f - ((float) lifeTime / MAX_LIFETIME);
    }
}
