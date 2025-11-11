package io.nghlong3004.model.type;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TileType {
    STONE(0),
    FLOOR(1),
    BRICK(2),
    GIFT_BOX(3);
    public final int id;

    public String getAssetKey() {
        return this.name()
                   .toLowerCase();
    }
}
