package io.nghlong3004.ai;

import io.nghlong3004.model.Bomber;
import io.nghlong3004.model.TileMap;
import lombok.Getter;

import java.util.List;

@Getter
public class AIDecision {
    public enum Action {
        MOVE_TO_TARGET,
        ESCAPE_DANGER,
        PLACE_BOMB,
        PLACE_BOMB_AND_ESCAPE,
        IDLE
    }

    private final Action action;
    private final List<PathNode> path;
    private final Bomber target;

    private AIDecision(Action action, List<PathNode> path, Bomber target) {
        this.action = action;
        this.path = path;
        this.target = target;
    }

    public static AIDecision moveToTarget(List<PathNode> path, Bomber target) {
        return new AIDecision(Action.MOVE_TO_TARGET, path, target);
    }

    public static AIDecision escapeDanger(List<PathNode> path) {
        return new AIDecision(Action.ESCAPE_DANGER, path, null);
    }

    public static AIDecision placeBomb() {
        return new AIDecision(Action.PLACE_BOMB, null, null);
    }

    public static AIDecision placeBombAndEscape(List<PathNode> escapePath) {
        return new AIDecision(Action.PLACE_BOMB_AND_ESCAPE, escapePath, null);
    }

    public static AIDecision idle() {
        return new AIDecision(Action.IDLE, null, null);
    }


    public int getNextDirection(Bomber bomber, TileMap tileMap) {
        if (path == null || path.isEmpty()) {
            return -1;
        }

        PathNode nextNode = path.get(0);
        int currentRow = PathFinder.getGridRow(bomber, tileMap);
        int currentCol = PathFinder.getGridCol(bomber, tileMap);

        int deltaRow = nextNode.getRow() - currentRow;
        int deltaCol = nextNode.getCol() - currentCol;


        if (deltaRow < 0) {
            return 3;
        }
        if (deltaRow > 0) {
            return 0;
        }
        if (deltaCol < 0) {
            return 1;
        }
        if (deltaCol > 0) {
            return 2;
        }

        return -1;
    }
}
