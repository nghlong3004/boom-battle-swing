package io.nghlong3004.entity;

import lombok.Getter;
import lombok.Setter;

import static io.nghlong3004.constant.GameConstant.TILE_SIZE;

@Getter
@Setter
public class Explosion {
    private int gridX;
    private int gridY;
    private int pixelX;
    private int pixelY;
    private int animationTick;
    private int animationFrame;
    private boolean finished;
    
    private static final int ANIMATION_SPEED = 3;
    private static final int TOTAL_FRAMES = 10;

    public Explosion(int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.pixelX = gridY * TILE_SIZE;
        this.pixelY = gridX * TILE_SIZE;
        this.animationTick = 0;
        this.animationFrame = 0;
        this.finished = false;
    }

    public void update() {
        if (finished) {
            return;
        }

        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationFrame++;
            
            if (animationFrame >= TOTAL_FRAMES) {
                finished = true;
            }
        }
    }

    public boolean shouldBeRemoved() {
        return finished;
    }
}
