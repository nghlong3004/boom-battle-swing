package io.nghlong3004.model;

public enum ItemType {
    BOMB("item_bomb", "Bomb", 0),
    BOMB_SIZE("item_bombsize", "BombSize", 1),
    SHOE("item_shoe", "Shoe", 2);

    public final String imageName;
    public final String displayName;
    public final int index;

    ItemType(String imageName, String displayName, int index) {
        this.imageName = imageName;
        this.displayName = displayName;
        this.index = index;
    }
}
