package io.nghlong3004.entity;

import io.nghlong3004.type.SkinType;
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

    public Bomber(float x, float y, SkinType skinType) {
        super(x, y, BOMBER_WIDTH, BOMBER_HEIGHT, skinType);
    }

    @Override
    protected float getDefaultSpeed() {
        return 1;
    }
}
