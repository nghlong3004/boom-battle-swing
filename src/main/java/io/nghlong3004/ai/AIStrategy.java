package io.nghlong3004.ai;

import io.nghlong3004.model.Bomb;
import io.nghlong3004.model.Bomber;
import io.nghlong3004.model.Explosion;
import io.nghlong3004.model.TileMap;
import io.nghlong3004.util.CollisionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class AIStrategy {

    private static final int SAFE_BOMB_DISTANCE = 3;
    private static final int ATTACK_RANGE = 6;

    public static AIDecision makeDecision(Bomber bomber, List<Bomber> allBombers, List<Bomb> bombs,
                                          List<Explosion> explosions, TileMap tileMap) {

        if (tileMap == null || tileMap.getData() == null) {
            log.warn("AI cannot make decision: TileMap is null");
            return AIDecision.idle();
        }

        int currentRow = PathFinder.getGridRow(bomber, tileMap);
        int currentCol = PathFinder.getGridCol(bomber, tileMap);

        log.trace("AI decision for bomber at [{},{}]", currentRow, currentCol);

        DangerMap dangerMap = new DangerMap(tileMap, bombs, explosions);


        if (dangerMap.isDangerous(currentRow, currentCol)) {
            log.debug("AI in danger at [{},{}], seeking escape", currentRow, currentCol);
            PathNode safeTile = PathFinder.findNearestSafeTile(bomber, tileMap, dangerMap);
            if (safeTile != null) {
                List<PathNode> escapePath = PathFinder.findSafePath(bomber, safeTile.getRow(), safeTile.getCol(),
                                                                    tileMap, dangerMap, bomber.getPathfindingMode(),
                                                                    true);
                if (escapePath != null && !escapePath.isEmpty()) {
                    log.info("AI ({}) escaping danger from [{},{}] to [{},{}]", bomber.getPathfindingMode(), currentRow,
                             currentCol, safeTile.getRow(), safeTile.getCol());
                    return AIDecision.escapeDanger(escapePath);
                }
            }
            log.warn("AI trapped at [{},{}], no escape route!", currentRow, currentCol);
        }

        Bomber target = PathFinder.findNearestEnemy(bomber, allBombers);
        if (target != null) {
            int targetRow = PathFinder.getGridRow(target, tileMap);
            int targetCol = PathFinder.getGridCol(target, tileMap);

            int distance = Math.abs(targetRow - currentRow) + Math.abs(targetCol - currentCol);

            log.trace("AI found target at [{},{}], distance={}", targetRow, targetCol, distance);


            if (distance <= 2 && bomber.canPlaceBomb()) {
                if (hasAdjacentSafeTile(bomber, currentRow, currentCol, tileMap, bombs, explosions)) {
                    log.info("AI placing bomb at [{},{}] near enemy (has escape route)", currentRow, currentCol);
                    return AIDecision.placeBomb();
                }
                else {
                    log.debug("AI too close to enemy [{},{}] but no adjacent safe tile - backing off", targetRow,
                              targetCol);

                    PathNode safeTile = findSafeTileAwayFrom(bomber, targetRow, targetCol, tileMap, dangerMap);
                    if (safeTile != null) {
                        List<PathNode> retreatPath = PathFinder.findSafePath(bomber, safeTile.getRow(),
                                                                             safeTile.getCol(), tileMap, dangerMap,
                                                                             bomber.getPathfindingMode());
                        if (retreatPath != null && !retreatPath.isEmpty()) {
                            log.debug("AI ({}) retreating to [{},{}]", bomber.getPathfindingMode(), safeTile.getRow(),
                                      safeTile.getCol());
                            return AIDecision.moveToTarget(retreatPath, null);
                        }
                    }
                }
            }


            if (distance > 2 && distance <= ATTACK_RANGE) {
                List<PathNode> attackPath = PathFinder.findSafePath(bomber, targetRow, targetCol, tileMap, dangerMap,
                                                                    bomber.getPathfindingMode());
                if (attackPath != null && !attackPath.isEmpty()) {
                    log.debug("AI ({}) pursuing target: [{},{}] -> [{},{}], path length={}",
                              bomber.getPathfindingMode(), currentRow, currentCol, targetRow, targetCol,
                              attackPath.size());
                    return AIDecision.moveToTarget(attackPath, target);
                }
                else {
                    log.trace("AI cannot find safe path to target");
                }
            }
        }
        else {
            log.trace("AI found no targets");
        }

        PathNode nearestDestructible = findNearestDestructibleBlock(bomber, tileMap, dangerMap);
        if (nearestDestructible != null) {
            int blockRow = nearestDestructible.getRow();
            int blockCol = nearestDestructible.getCol();
            int distance = Math.abs(blockRow - currentRow) + Math.abs(blockCol - currentCol);

            log.trace("AI found destructible at [{},{}], distance={}", blockRow, blockCol, distance);
            if (distance <= 1 && bomber.canPlaceBomb()) {

                if (hasAdjacentSafeTile(bomber, currentRow, currentCol, tileMap, bombs, explosions)) {
                    log.debug("AI placing bomb at [{},{}] to destroy block", currentRow, currentCol);
                    return AIDecision.placeBomb();
                }
            }

            List<PathNode> path = PathFinder.findSafePath(bomber, blockRow, blockCol, tileMap, dangerMap,
                                                          bomber.getPathfindingMode());
            if (path != null && !path.isEmpty()) {
                log.debug("AI ({}) moving to destructible: [{},{}] -> [{},{}]", bomber.getPathfindingMode(), currentRow,
                          currentCol, blockRow, blockCol);
                return AIDecision.moveToTarget(path, null);
            }
        }


        PathNode randomSafeTile = findRandomSafeTile(bomber, tileMap, dangerMap, currentRow, currentCol);
        if (randomSafeTile != null) {
            List<PathNode> wanderPath = PathFinder.findSafePath(bomber, randomSafeTile.getRow(),
                                                                randomSafeTile.getCol(), tileMap, dangerMap,
                                                                bomber.getPathfindingMode());
            if (wanderPath != null && !wanderPath.isEmpty()) {
                log.trace("AI ({}) wandering to [{},{}]", bomber.getPathfindingMode(), randomSafeTile.getRow(),
                          randomSafeTile.getCol());
                return AIDecision.moveToTarget(wanderPath, target);
            }
        }

        log.trace("AI idle at [{},{}]", currentRow, currentCol);
        return AIDecision.idle();
    }


    private static PathNode findSafeTileAwayFrom(Bomber bomber, int enemyRow, int enemyCol, TileMap tileMap,
                                                 DangerMap dangerMap) {
        int currentRow = PathFinder.getGridRow(bomber, tileMap);
        int currentCol = PathFinder.getGridCol(bomber, tileMap);


        int deltaRow = currentRow - enemyRow;
        int deltaCol = currentCol - enemyCol;


        int[][] retreatDirections = {{deltaRow > 0 ? 1 : -1, 0}, {0, deltaCol > 0 ? 1 : -1},
                {deltaRow > 0 ? 1 : -1, deltaCol > 0 ? 1 : -1}};

        for (int[] dir : retreatDirections) {
            for (int dist = 1; dist <= 3; dist++) {
                int retreatRow = currentRow + dir[0] * dist;
                int retreatCol = currentCol + dir[1] * dist;

                if (!dangerMap.isDangerous(retreatRow, retreatCol) && !CollisionUtil.isSolid(retreatRow, retreatCol,
                                                                                             tileMap)) {
                    return new PathNode(retreatRow, retreatCol);
                }
            }
        }


        return PathFinder.findNearestSafeTile(bomber, tileMap, dangerMap);
    }

    private static PathNode findRandomSafeTile(Bomber bomber, TileMap tileMap, DangerMap dangerMap, int currentRow,
                                               int currentCol) {
        int[][] data = tileMap.getData();
        java.util.Random random = new java.util.Random();

        for (int attempts = 0; attempts < 10; attempts++) {
            int randomRow = random.nextInt(data.length);
            int randomCol = random.nextInt(data[0].length);

            if (!dangerMap.isDangerous(randomRow, randomCol) && !CollisionUtil.isSolid(randomRow, randomCol,
                                                                                       tileMap) && (randomRow != currentRow || randomCol != currentCol)) {
                return new PathNode(randomRow, randomCol);
            }
        }

        return null;
    }

    private static boolean hasAdjacentSafeTile(Bomber bomber, int bombRow, int bombCol, TileMap tileMap,
                                               List<Bomb> existingBombs, List<Explosion> explosions) {
        List<Bomb> simulatedBombs = new java.util.ArrayList<>(existingBombs);

        int xOffset = tileMap.getXDrawOffSet();
        int yOffset = tileMap.getYDrawOffSet();
        float bombCenterX = bombCol * 48 + xOffset + 24;
        float bombCenterY = bombRow * 48 + yOffset + 24;

        Bomb tempBomb = new Bomb(bombCenterX, bombCenterY, xOffset, yOffset);
        simulatedBombs.add(tempBomb);

        DangerMap futureMap = new DangerMap(tileMap, simulatedBombs, explosions);

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int adjRow = bombRow + dir[0];
            int adjCol = bombCol + dir[1];

            if (!futureMap.isDangerous(adjRow, adjCol) && !CollisionUtil.isSolid(adjRow, adjCol, tileMap)) {
                log.trace("AI has safe adjacent tile at [{},{}]", adjRow, adjCol);
                return true;
            }
        }

        log.trace("AI has no safe adjacent tiles");
        return false;
    }

    private static List<PathNode> getEscapePathAfterBomb(Bomber bomber, int bombRow, int bombCol, TileMap tileMap,
                                                         List<Bomb> existingBombs, List<Explosion> explosions) {
        List<Bomb> simulatedBombs = new java.util.ArrayList<>(existingBombs);
        int xOffset = tileMap.getXDrawOffSet();
        int yOffset = tileMap.getYDrawOffSet();
        float bombCenterX = bombCol * 48 + xOffset + 24;
        float bombCenterY = bombRow * 48 + yOffset + 24;

        Bomb tempBomb = new Bomb(bombCenterX, bombCenterY, xOffset, yOffset);
        simulatedBombs.add(tempBomb);

        DangerMap futureMap = new DangerMap(tileMap, simulatedBombs, explosions);

        PathNode safeTile = PathFinder.findNearestSafeTile(bomber, tileMap, futureMap);

        if (safeTile == null) {
            log.trace("No safe tile found after placing bomb at [{},{}]", bombRow, bombCol);
            return null;
        }

        if (safeTile.getDistance() > SAFE_BOMB_DISTANCE) {
            log.trace("Safe tile too far: {} tiles (max {})", safeTile.getDistance(), SAFE_BOMB_DISTANCE);
            return null;
        }

        List<PathNode> escapePath = PathFinder.findSafePath(bomber, safeTile.getRow(), safeTile.getCol(), tileMap,
                                                            futureMap, bomber.getPathfindingMode(), true);

        if (escapePath == null || escapePath.isEmpty()) {
            log.trace("No valid path to safe tile at [{},{}]", safeTile.getRow(), safeTile.getCol());
            return null;
        }

        log.debug("AI ({}) can escape: bomb at [{},{}] → safe tile [{},{}], path length={}",
                  bomber.getPathfindingMode(), bombRow, bombCol, safeTile.getRow(), safeTile.getCol(),
                  escapePath.size());
        return escapePath;
    }

    private static boolean canEscapeAfterBomb(Bomber bomber, int bombRow, int bombCol, TileMap tileMap,
                                              List<Bomb> existingBombs, List<Explosion> explosions) {
        return hasAdjacentSafeTile(bomber, bombRow, bombCol, tileMap, existingBombs, explosions);
    }

    private static PathNode findNearestDestructibleBlock(Bomber bomber, TileMap tileMap, DangerMap dangerMap) {
        int startRow = PathFinder.getGridRow(bomber, tileMap);
        int startCol = PathFinder.getGridCol(bomber, tileMap);
        int[][] data = tileMap.getData();

        PathNode nearest = null;
        int minDistance = Integer.MAX_VALUE;

        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                if (CollisionUtil.isDestructible(row, col, tileMap)) {
                    int dist = Math.abs(row - startRow) + Math.abs(col - startCol);
                    if (dist < minDistance && !dangerMap.isDangerous(row, col)) {
                        minDistance = dist;
                        nearest = new PathNode(row, col);
                    }
                }
            }
        }

        return nearest;
    }
}
