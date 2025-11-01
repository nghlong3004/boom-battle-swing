package io.nghlong3004.model;

import io.nghlong3004.ai.AIPathfindingMode;
import lombok.Getter;
import lombok.Setter;

import static io.nghlong3004.constant.BomberConstant.BOMBER_SPEED;

@Getter
@Setter
public class Bomber extends Entity {
    private int maxBombs = 1;
    private int currentBombs = 0;
    private boolean placeBombRequested = false;
    private boolean isPlayer;
    private int skinIndex = 0;

    private int aiTick = 0;
    private int directionChangeCooldown = 0;
    private AIPathfindingMode pathfindingMode = AIPathfindingMode.BFS;


    private int deathAnimationFrame = 0;
    private int deathAnimationTick = 0;
    private boolean isDead = false;

    public Bomber(float x, float y, int width, int height, boolean isPlayer) {
        super(x, y, width, height);
        this.isPlayer = isPlayer;
    }

    public Bomber(float x, float y, int width, int height, boolean isPlayer, int skinIndex) {
        super(x, y, width, height);
        this.isPlayer = isPlayer;
        this.skinIndex = skinIndex;

        this.pathfindingMode = getPathfindingModeForSkin(skinIndex);
    }

    private static AIPathfindingMode getPathfindingModeForSkin(int skinIndex) {
        return switch (skinIndex) {
            case 1 -> AIPathfindingMode.ASTAR;
            case 2 -> AIPathfindingMode.DIJKSTRA;
            case 3 -> AIPathfindingMode.BFS;
            default -> AIPathfindingMode.BFS;
        };
    }

    @Override
    protected float getDefaultSpeed() {
        return BOMBER_SPEED;
    }

    public boolean canPlaceBomb() {
        return currentBombs < maxBombs;
    }

    public void requestPlaceBomb() {
        if (canPlaceBomb()) {
            this.placeBombRequested = true;
        }
    }

    public void incrementBombCount() {
        currentBombs++;
    }

    public void decrementBombCount() {
        if (currentBombs > 0) {
            currentBombs--;
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.currentBombs = 0;
        this.placeBombRequested = false;
        this.aiTick = 0;
        this.directionChangeCooldown = 0;
        this.isDead = false;
        this.deathAnimationFrame = 0;
        this.deathAnimationTick = 0;
    }


    public void die() {
        if (!isDead) {
            this.isDead = true;
            this.setAlive(false);
            this.deathAnimationFrame = 0;
            this.deathAnimationTick = 0;
        }
    }


    public void updateDeathAnimation() {
        if (!isDead) {
            return;
        }

        deathAnimationTick++;
        if (deathAnimationTick >= 100) {
            deathAnimationTick = 0;
            deathAnimationFrame = (deathAnimationFrame + 1) % 4;
        }
    }


    public boolean isDeathAnimationComplete() {
        return isDead && deathAnimationFrame >= 15;
    }
}
