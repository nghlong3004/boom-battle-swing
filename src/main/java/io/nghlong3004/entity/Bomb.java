package io.nghlong3004.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static io.nghlong3004.constant.GameConstant.TILE_SIZE;

@Slf4j
@Getter
@Setter
public class Bomb {
    private int gridX;
    private int gridY;
    private int pixelX;
    private int pixelY;
    private int explosionRange;
    private int timer;
    private boolean exploded;
    private Bomber owner;
    private int animationTick;
    private int animationFrame;
    private boolean solid;
    private static final int EXPLOSION_TIME = 180;
    private static final int ANIMATION_SPEED = 20;

    public Bomb(int gridX, int gridY, int explosionRange, Bomber owner) {
        this.gridX = gridX;
        this.gridY = gridY;

        this.pixelX = gridY * TILE_SIZE;
        this.pixelY = gridX * TILE_SIZE;
        this.explosionRange = explosionRange;
        this.owner = owner;
        this.timer = EXPLOSION_TIME;
        this.exploded = false;
        this.animationTick = 0;
        this.animationFrame = 0;
        this.solid = false;
    }

    public void update() {
        if (exploded) {
            return;
        }
        timer--;
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationFrame = (animationFrame + 1) % 3;
        }
        if (timer <= 0) {
            explode();
        }
    }

    public void explode() {
        if (!exploded) {
            exploded = true;
            log.debug("Bomb exploding at grid ({}, {})", gridX, gridY);

            if (owner != null) {
                owner.setCurrentBombs(owner.getCurrentBombs() - 1);
            }
        }
    }

    public boolean shouldBeRemoved() {
        return exploded;
    }
}
