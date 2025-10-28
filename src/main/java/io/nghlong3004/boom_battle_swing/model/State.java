package io.nghlong3004.boom_battle_swing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum State {
    ALIVE("alive"),
    DEAD("dead");
    private final String mean;
}
