package io.nghlong3004.ai;

import io.nghlong3004.model.Bomber;
import io.nghlong3004.model.TileMap;
import io.nghlong3004.util.CollisionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static io.nghlong3004.constant.GameConstant.TILES_SIZE;

@Slf4j
public class PathFinder {

    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};


    public static List<PathNode> findSafePath(Bomber bomber, int targetRow, int targetCol, TileMap tileMap,
                                              DangerMap dangerMap, AIPathfindingMode mode) {
        return findSafePath(bomber, targetRow, targetCol, tileMap, dangerMap, mode, false);
    }


    public static List<PathNode> findSafePath(Bomber bomber, int targetRow, int targetCol, TileMap tileMap,
                                              DangerMap dangerMap, AIPathfindingMode mode,
                                              boolean allowDangerousStart) {
        return switch (mode) {
            case ASTAR -> findPathAStar(bomber, targetRow, targetCol, tileMap, dangerMap, allowDangerousStart);
            case DIJKSTRA -> findPathDijkstra(bomber, targetRow, targetCol, tileMap, dangerMap, allowDangerousStart);
            case BFS -> findPathBFS(bomber, targetRow, targetCol, tileMap, dangerMap, allowDangerousStart);
        };
    }


    private static List<PathNode> findPathAStar(Bomber bomber, int targetRow, int targetCol, TileMap tileMap,
                                                DangerMap dangerMap, boolean allowDangerousStart) {
        int startRow = getGridRow(bomber, tileMap);
        int startCol = getGridCol(bomber, tileMap);

        if (startRow == targetRow && startCol == targetCol) {
            return Collections.emptyList();
        }


        PriorityQueue<PathNode> openSet = new PriorityQueue<>();
        Map<String, Integer> bestCost = new HashMap<>();

        int h = PathNode.manhattanDistance(startRow, startCol, targetRow, targetCol);
        PathNode start = new PathNode(startRow, startCol, null, 0, h);
        openSet.offer(start);
        bestCost.put(startRow + "," + startCol, 0);

        while (!openSet.isEmpty()) {
            PathNode current = openSet.poll();


            if (current.getRow() == targetRow && current.getCol() == targetCol) {
                List<PathNode> path = reconstructPath(current);
                log.debug("A* found path: length={}, cost={}", path.size(), current.getDistance());
                return path;
            }

            String currentKey = current.getRow() + "," + current.getCol();


            if (bestCost.get(currentKey) < current.getDistance()) {
                continue;
            }


            for (int[] dir : DIRECTIONS) {
                int newRow = current.getRow() + dir[0];
                int newCol = current.getCol() + dir[1];
                String key = newRow + "," + newCol;

                if (!isWalkable(newRow, newCol, tileMap)) {
                    continue;
                }

                boolean isDangerous = dangerMap.isDangerous(newRow, newCol);
                boolean isStartPosition = (newRow == startRow && newCol == startCol);

                if (isDangerous && !(allowDangerousStart && isStartPosition)) {
                    continue;
                }

                int newDistance = current.getDistance() + 1;


                if (bestCost.containsKey(key) && bestCost.get(key) <= newDistance) {
                    continue;
                }

                int heuristic = PathNode.manhattanDistance(newRow, newCol, targetRow, targetCol);
                PathNode next = new PathNode(newRow, newCol, current, newDistance, heuristic);
                openSet.offer(next);
                bestCost.put(key, newDistance);
            }
        }

        log.trace("A* no path found from [{},{}] to [{},{}]", startRow, startCol, targetRow, targetCol);
        return null;
    }


    private static List<PathNode> findPathDijkstra(Bomber bomber, int targetRow, int targetCol, TileMap tileMap,
                                                   DangerMap dangerMap, boolean allowDangerousStart) {
        int startRow = getGridRow(bomber, tileMap);
        int startCol = getGridCol(bomber, tileMap);

        if (startRow == targetRow && startCol == targetCol) {
            return Collections.emptyList();
        }


        PriorityQueue<PathNode> openSet = new PriorityQueue<>();
        Map<String, Integer> distances = new HashMap<>();

        PathNode start = new PathNode(startRow, startCol, null, 0, true);
        openSet.offer(start);
        distances.put(startRow + "," + startCol, 0);

        while (!openSet.isEmpty()) {
            PathNode current = openSet.poll();


            if (current.getRow() == targetRow && current.getCol() == targetCol) {
                List<PathNode> path = reconstructPath(current);
                log.debug("Dijkstra found path: length={}, cost={}", path.size(), current.getDistance());
                return path;
            }

            String currentKey = current.getRow() + "," + current.getCol();


            if (distances.get(currentKey) < current.getDistance()) {
                continue;
            }


            for (int[] dir : DIRECTIONS) {
                int newRow = current.getRow() + dir[0];
                int newCol = current.getCol() + dir[1];
                String key = newRow + "," + newCol;

                if (!isWalkable(newRow, newCol, tileMap)) {
                    continue;
                }

                boolean isDangerous = dangerMap.isDangerous(newRow, newCol);
                boolean isStartPosition = (newRow == startRow && newCol == startCol);

                if (isDangerous && !(allowDangerousStart && isStartPosition)) {
                    continue;
                }

                int newDistance = current.getDistance() + 1;

                if (!distances.containsKey(key) || newDistance < distances.get(key)) {
                    PathNode next = new PathNode(newRow, newCol, current, newDistance, true);
                    openSet.offer(next);
                    distances.put(key, newDistance);
                }
            }
        }

        log.trace("Dijkstra no path found from [{},{}] to [{},{}]", startRow, startCol, targetRow, targetCol);
        return null;
    }


    private static List<PathNode> findPathBFS(Bomber bomber, int targetRow, int targetCol, TileMap tileMap,
                                              DangerMap dangerMap, boolean allowDangerousStart) {
        int startRow = getGridRow(bomber, tileMap);
        int startCol = getGridCol(bomber, tileMap);

        if (startRow == targetRow && startCol == targetCol) {
            return Collections.emptyList();
        }

        Queue<PathNode> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        PathNode start = new PathNode(startRow, startCol, null, 0);
        queue.offer(start);
        visited.add(startRow + "," + startCol);

        while (!queue.isEmpty()) {
            PathNode current = queue.poll();

            if (current.getRow() == targetRow && current.getCol() == targetCol) {
                List<PathNode> path = reconstructPath(current);
                log.debug("BFS found path: length={}", path.size());
                return path;
            }

            for (int[] dir : DIRECTIONS) {
                int newRow = current.getRow() + dir[0];
                int newCol = current.getCol() + dir[1];
                String key = newRow + "," + newCol;

                if (visited.contains(key)) {
                    continue;
                }

                if (!isWalkable(newRow, newCol, tileMap)) {
                    continue;
                }

                boolean isDangerous = dangerMap.isDangerous(newRow, newCol);
                boolean isStartPosition = (newRow == startRow && newCol == startCol);

                if (isDangerous && !(allowDangerousStart && isStartPosition)) {
                    continue;
                }

                PathNode next = new PathNode(newRow, newCol, current, current.getDistance() + 1);
                queue.offer(next);
                visited.add(key);
            }
        }

        log.trace("BFS no path found from [{},{}] to [{},{}]", startRow, startCol, targetRow, targetCol);
        return null;
    }


    public static List<PathNode> findSafePath(Bomber bomber, int targetRow, int targetCol, TileMap tileMap,
                                              DangerMap dangerMap) {
        return findSafePath(bomber, targetRow, targetCol, tileMap, dangerMap, AIPathfindingMode.BFS, false);
    }

    public static List<PathNode> findSafePath(Bomber bomber, int targetRow, int targetCol, TileMap tileMap,
                                              DangerMap dangerMap, boolean allowDangerousStart) {
        return findSafePath(bomber, targetRow, targetCol, tileMap, dangerMap, AIPathfindingMode.BFS,
                            allowDangerousStart);
    }

    public static PathNode findNearestSafeTile(Bomber bomber, TileMap tileMap, DangerMap dangerMap) {
        int startRow = getGridRow(bomber, tileMap);
        int startCol = getGridCol(bomber, tileMap);

        Queue<PathNode> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        PathNode start = new PathNode(startRow, startCol, null, 0);
        queue.offer(start);
        visited.add(startRow + "," + startCol);

        while (!queue.isEmpty()) {
            PathNode current = queue.poll();

            if (!dangerMap.isDangerous(current.getRow(), current.getCol())) {
                return current;
            }

            for (int[] dir : DIRECTIONS) {
                int newRow = current.getRow() + dir[0];
                int newCol = current.getCol() + dir[1];
                String key = newRow + "," + newCol;

                if (visited.contains(key)) {
                    continue;
                }

                if (isWalkable(newRow, newCol, tileMap)) {
                    PathNode next = new PathNode(newRow, newCol, current, current.getDistance() + 1);
                    queue.offer(next);
                    visited.add(key);
                }
            }
        }

        return null;
    }

    public static Bomber findNearestEnemy(Bomber self, List<Bomber> allBombers) {
        Bomber nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Bomber other : allBombers) {
            if (other == self || !other.isAlive()) {
                continue;
            }

            double dist = distance(self, other);
            if (dist < minDistance) {
                minDistance = dist;
                nearest = other;
            }
        }

        return nearest;
    }

    private static List<PathNode> reconstructPath(PathNode end) {
        List<PathNode> path = new ArrayList<>();
        PathNode current = end;

        while (current != null) {
            path.add(current);
            current = current.getParent();
        }

        Collections.reverse(path);

        if (!path.isEmpty() && path.get(0).getParent() == null) {
            path.remove(0);
        }

        return path;
    }

    private static boolean isWalkable(int row, int col, TileMap tileMap) {
        int[][] data = tileMap.getData();

        if (row < 0 || row >= data.length || col < 0 || col >= data[0].length) {
            return false;
        }

        return !CollisionUtil.isSolid(row, col, tileMap);
    }

    public static int getGridRow(Bomber bomber, TileMap tileMap) {
        float centerY = bomber.getBox().y + bomber.getBox().height / 2f;
        float relativeY = centerY - tileMap.getYDrawOffSet();
        return (int) (relativeY / TILES_SIZE);
    }

    public static int getGridCol(Bomber bomber, TileMap tileMap) {
        float centerX = bomber.getBox().x + bomber.getBox().width / 2f;
        float relativeX = centerX - tileMap.getXDrawOffSet();
        return (int) (relativeX / TILES_SIZE);
    }

    private static double distance(Bomber a, Bomber b) {
        double dx = a.getBox().x - b.getBox().x;
        double dy = a.getBox().y - b.getBox().y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}