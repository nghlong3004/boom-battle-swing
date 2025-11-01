package io.nghlong3004.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static io.nghlong3004.constant.GameConstant.TILES_SIZE;

@Getter
@AllArgsConstructor
public class SpawnPoint {
    private final int gridRow;
    private final int gridCol;
    private final float x;
    private final float y;
    private final int spawnType;


    public float getCenteredX(int entityWidth) {
        return x + (TILES_SIZE - entityWidth) / 2f;
    }


    public float getCenteredY(int entityHeight) {
        return y + (TILES_SIZE - entityHeight) / 2f;
    }
}
