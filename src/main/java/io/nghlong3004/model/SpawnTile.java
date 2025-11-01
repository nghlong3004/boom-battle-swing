package io.nghlong3004.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpawnTile {
    PLAYER(6, "player"),
    SOLDIER_1(7, "soldier_1"),
    SOLDIER_2(8, "soldier_2"),
    SOLDIER_3(9, "soldier_3");
    
    private final int value;
    private final String name;
    
    public static boolean isSpawnTile(int value) {
        return value >= 6 && value <= 9;
    }
    
    public static SpawnTile fromValue(int value) {
        for (SpawnTile tile : values()) {
            if (tile.value == value) {
                return tile;
            }
        }
        return null;
    }
}
