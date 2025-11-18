package io.nghlong3004.model.entities;

import io.nghlong3004.model.type.MonsterType;

public class Monster extends Bomber {

    private final MonsterType monsterType;

    public Monster(float x, float y, MonsterType monsterType) {
        super(x, y, null);
        this.monsterType = monsterType;
    }

    @Override
    public int getSkinId() {
        return monsterType.getId();
    }
}
