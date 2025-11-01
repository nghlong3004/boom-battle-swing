package io.nghlong3004.ai;

import lombok.Getter;

@Getter
public class PathNode implements Comparable<PathNode> {
    private final int row;
    private final int col;
    private final PathNode parent;
    private final int distance;
    private final int heuristic;
    private final int totalCost;

    public PathNode(int row, int col, PathNode parent, int distance) {
        this.row = row;
        this.col = col;
        this.parent = parent;
        this.distance = distance;
        this.heuristic = 0;
        this.totalCost = distance;
    }


    public PathNode(int row, int col, PathNode parent, int distance, int heuristic) {
        this.row = row;
        this.col = col;
        this.parent = parent;
        this.distance = distance;
        this.heuristic = heuristic;
        this.totalCost = distance + heuristic;
    }


    public PathNode(int row, int col, PathNode parent, int distance, boolean useCost) {
        this.row = row;
        this.col = col;
        this.parent = parent;
        this.distance = distance;
        this.heuristic = 0;
        this.totalCost = distance;
    }

    public PathNode(int row, int col) {
        this(row, col, null, 0);
    }

    /**
     * Compare based on totalCost for priority queue (A* and Dijkstra)
     * Lower cost = higher priority
     */
    @Override
    public int compareTo(PathNode other) {
        return Integer.compare(this.totalCost, other.totalCost);
    }

    /**
     * Manhattan distance heuristic for A*
     */
    public static int manhattanDistance(int row1, int col1, int row2, int col2) {
        return Math.abs(row1 - row2) + Math.abs(col1 - col2);
    }
}
