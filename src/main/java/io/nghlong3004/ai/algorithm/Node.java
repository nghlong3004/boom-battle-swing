package io.nghlong3004.ai.algorithm;

import lombok.Getter;

import java.awt.*;

@Getter
public class Node {
    protected Node parent;
    protected Point location;
    protected Character action;

    protected int h = 0;
    protected int g = 0;
    protected int f = 0;

    protected Node(Node parent, Point location, Character action) {
        this.parent = parent;
        this.location = location;
        this.action = action;
    }


}
