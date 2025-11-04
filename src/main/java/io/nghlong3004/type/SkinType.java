package io.nghlong3004.type;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SkinType {
    BOZ(0),
    EVIE(1),
    IKE(2),
    PLUNK(3);
    public final int id;

    public String getAssetKey() {
        return this.name()
                   .toLowerCase();
    }
}
