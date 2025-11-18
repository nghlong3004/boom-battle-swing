package io.nghlong3004.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MonsterType {
    MONSTER(4);
    @Getter
    private final int id;
    
    public String getAssetKey() {
        return this.name()
                   .toLowerCase();
    }
}
