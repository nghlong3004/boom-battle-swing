package io.nghlong3004.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Skin {
    BOZ(0),
    EVIE(1),
    IKE(2),
    PLUNK(3);
    public final int id;

    public String getAssetKey() {
        return this.name().toLowerCase();
    }
}
