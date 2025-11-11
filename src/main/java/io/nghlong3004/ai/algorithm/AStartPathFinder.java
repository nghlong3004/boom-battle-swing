package io.nghlong3004.ai.algorithm;

import io.nghlong3004.game.manager.MapManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AStartPathFinder implements PathFinder {

    private final MapManager mapManager;

    @Override
    public List<Node> findPath(Point start, Point end) {
        var openList = new ArrayList<Node>();
        var closedList = new ArrayList<Node>();
        openList.add(new Node(null, start, null));

        final int maxLoops = 1000;
        int counter = 0;

        while (!openList.isEmpty() && counter <= maxLoops) {
            int currIndex = 0;
            Node currNode = openList.getFirst();
            for (int i = 1; i < openList.size(); i++) {
                if (openList.get(i).f < currNode.f) {
                    currNode = openList.get(i);
                    currIndex = i;
                }
            }
            if (currNode.location.equals(end)) {
                return PathHelper.getPath(currNode);
            }

            openList.remove(currIndex);
            closedList.add(currNode);

            var neighbors = PathHelper.getFreeNeighbors(mapManager.getMap(), currNode.location);
            List<Node> neighborNodes = new ArrayList<>(neighbors.size());
            for (var neighbor : neighbors) {
                neighborNodes.add(new Node(null, neighbor.getKey(), neighbor.getValue()));
            }

            for (Node neighbor : neighborNodes) {
                int cost = currNode.g + 1;
                boolean inClosed = isNeighbor(neighbor, openList, cost);
                boolean inOpen = isNeighbor(neighbor, closedList, cost);

                if (!inOpen && !inClosed) {
                    neighbor.g = cost;
                    neighbor.h = Distance.manhattan(neighbor.location, end);
                    neighbor.f = neighbor.g + neighbor.h;
                    neighbor.parent = currNode;
                    openList.add(neighbor);
                }
            }
            counter++;
        }

        return List.of();
    }

    private boolean isNeighbor(Node neighbor, List<Node> nodes, int cost) {
        for (int i = 0; i < nodes.size(); ++i) {
            Node node = nodes.get(i);
            if (neighbor.location.equals(node.location) && cost < neighbor.g) {
                nodes.remove(i);
                return true;
            }
        }
        return false;
    }
}
