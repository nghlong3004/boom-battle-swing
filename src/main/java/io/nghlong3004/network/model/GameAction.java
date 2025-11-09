package io.nghlong3004.network.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameAction {
    private String playerId;
    private ActionType actionType;
    private int gridX;
    private int gridY;
    private int direction;
    private long timestamp;

    public enum ActionType {
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
        STOP,
        PLACE_BOMB
    }
}
