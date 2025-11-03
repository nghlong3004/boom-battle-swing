package io.nghlong3004.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bomber extends Entity {

    private int maxBombs = 1;
    private int currentBombs = 0;
    private int explosionRange = 1;
    private float speedBoost = 0f;
    private boolean placeBombRequested = false;
    private int skinIndex = 0;

    protected Bomber(float x, float y, int width, int height, int skinIndex) {
        super(x, y, width, height);
        this.skinIndex = skinIndex;
    }

    @Override
    protected float getDefaultSpeed() {
        return 0;
    }
}
