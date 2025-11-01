package io.nghlong3004.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Tile {
    STONE(0, "stone"),
    FLOOR(1, "floor"),
    BRICK(2, "brick"),
    GIFT_BOX(3, "gift_box"),
    M(4, "m");

    public final int index;
    public final String name;

}
