package io.nghlong3004.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Tile {
    STONE(0, "stone"),
    BRICK(2, "brick"),
    FLOOR(1, "floor"),
    GIFT_BOX(3, "gift_box"),
    M(4, "m");

    public final int index;
    public final String name;

}
