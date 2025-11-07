package io.nghlong3004.ai.algorithm;

import java.awt.*;

public class Distance {
    public static int manhattan(Point start, Point end) {
        return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
    }

    public static double euclidean(Point start, Point end) {
        return Math.sqrt(Math.pow(start.x - end.y, 2) + Math.pow(start.y - end.y, 2));
    }

    public static int chebyshev(Point start, Point end) {
        return Math.max(Math.abs(start.x - end.x), Math.abs(start.y - end.y));
    }
}
