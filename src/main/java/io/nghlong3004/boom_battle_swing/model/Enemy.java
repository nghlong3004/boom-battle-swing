package io.nghlong3004.boom_battle_swing.model;

import lombok.Getter;

import java.awt.*;

import static io.nghlong3004.boom_battle_swing.constant.BomberConstant.GetSpriteAmount;

public abstract class Enemy extends Entity {

    @Getter
    private int aniIndex, enemyState, enemyType;
    private int aniTick, aniSpeed = 25;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        setHitbox(x, y, width, height);

    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
                aniIndex = 0;
            }
        }
    }

    @Override
    public void update() {
        updateAnimationTick();
    }

    @Override
    public void render(Graphics graphics) {

    }
}
