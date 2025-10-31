package io.nghlong3004.boom_battle_swing.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TileMode {
    DESERT_MODE(0, "desert_mode"),
    LAND_MODE(1, "land_mode"),
    TOWN_MODE(2, "town_mode"),
    UNDERWATER_MODE(3, "underwater_mode"),
    XMAS_MODE(4, "xmas_mode"),
    N(5, "n");
    public final int index;
    public final String name;

    public static TileMode MODE = DESERT_MODE;

}
