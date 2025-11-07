package io.nghlong3004.ai.algorithm;

import io.nghlong3004.map.MapData;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PathHelper {
    public static List<Node> getPath(Node node) {
        var path = new ArrayList<Node>();
        while (node != null && node.parent != null) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public static List<Character> getPathActions(List<Node> path) {
        var actions = new ArrayList<Character>();
        for (var node : path) {
            actions.add(node.action);
        }
        return actions;
    }

    public static List<Map.Entry<Point, Character>> getFreeNeighbors(MapData mapData, Point location) {
        int x = location.x, y = location.y;
        var neighbors = List.of(Map.entry(new Point(x - 1, y), 'u'), Map.entry(new Point(x + 1, y), 'd'),
                                Map.entry(new Point(x, y - 1), 'l'), Map.entry(new Point(x, y + 1), 'r'));

        List<Map.Entry<Point, Character>> free = new ArrayList<>();
        for (var neighbor : neighbors) {
            Point point = neighbor.getKey();
            char direction = neighbor.getValue();
            if (point.x >= mapData.getData().length || point.x < 0) {
                continue;
            }
            if (point.y >= mapData.getData()[point.x].length || point.y < 0) {
                continue;
            }
            int tile = mapData.getData()[point.x][point.y];
            if (tile != 0 && tile != 2 && tile != 3) {
                free.add(Map.entry(point, direction));
            }
        }
        return free;
    }

}
