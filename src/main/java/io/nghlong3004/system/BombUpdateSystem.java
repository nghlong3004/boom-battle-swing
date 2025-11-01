package io.nghlong3004.system;

import io.nghlong3004.model.Bomb;

public class BombUpdateSystem implements UpdateSystem {

    private static final int BOMB_ANIMATION_SPEED = 20;

    @Override
    public void update(Object entity) {
        Bomb bomb = (Bomb) entity;

        if (bomb.isExploded()) {
            return;
        }

        updateAnimation(bomb);

        if (bomb.shouldExplode()) {
            bomb.explode();
        }
    }

    private void updateAnimation(Bomb bomb) {
        bomb.setTick(bomb.getTick() + 1);

        if (bomb.getTick() >= BOMB_ANIMATION_SPEED) {
            bomb.setTick(0);
            bomb.setIndex(bomb.getIndex() + 1);

            if (bomb.getIndex() >= 8) {
                bomb.setIndex(0);
            }
        }
    }
}
