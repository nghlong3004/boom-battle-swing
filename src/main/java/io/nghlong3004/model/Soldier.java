package io.nghlong3004.model;

import lombok.Getter;
import lombok.Setter;

import static io.nghlong3004.constant.SoldierConstant.SOLDIER_SPEED;

@Getter
@Setter
public class Soldier extends Entity {

    private int soldierType;
    private int aiTick;
    private int directionChangeCooldown;

    public Soldier(float x, float y, int width, int height, int soldierType) {
        super(x, y, width, height);
        this.soldierType = soldierType % 3 + 1;
        this.aiTick = 0;
        this.directionChangeCooldown = 0;
    }
    
    @Override
    protected float getDefaultSpeed() {
        return SOLDIER_SPEED;
    }

    @Override
    public void reset() {
        super.reset();
        this.aiTick = 0;
        this.directionChangeCooldown = 0;
    }
}
