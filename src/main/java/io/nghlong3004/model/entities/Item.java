package io.nghlong3004.model.entities;

import io.nghlong3004.model.type.ItemType;
import lombok.Getter;
import lombok.Setter;

import static io.nghlong3004.constant.GameConstant.TILE_SIZE;

@Getter
@Setter
public class Item {
    private int gridX;
    private int gridY;
    private ItemType type;
    private boolean collected;
    private int animationTick;
    private int animationFrame;
    private static final int ANIMATION_SPEED = 5;

    public Item(int gridX, int gridY, ItemType type) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.type = type;
        this.collected = false;
        this.animationTick = 0;
        this.animationFrame = 0;
    }

    public void update() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationFrame = (animationFrame + 1) % 8;
        }
    }

    public int getPixelX() {
        return gridY * TILE_SIZE;
    }

    public int getPixelY() {
        return gridX * TILE_SIZE;
    }

    public void applyEffect(Bomber bomber) {
        switch (type) {
            case ITEM_BOMB:
                bomber.setMaxBombs(bomber.getMaxBombs() + 1);
                break;
            case ITEM_BOMB_SIZE:
                bomber.setExplosionRange(bomber.getExplosionRange() + 1);
                break;
            case ITEM_SHOE:
                bomber.setSpeed(bomber.getSpeed() * 1.05f);
                bomber.setHasSpeedBoost(true);
                break;
        }
        this.collected = true;
    }
}
