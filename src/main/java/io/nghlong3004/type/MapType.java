package io.nghlong3004.type;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MapType {
    DESERT_MODE(0),
    LAND_MODE(1),
    TOWN_MODE(2),
    UNDERWATER_MODE(3),
    XMAS_MODE(4);
    public final int id;

    public String getAssetKey() {
        return this.name()
                   .toLowerCase();
    }
}
