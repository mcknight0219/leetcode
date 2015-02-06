package com.company;

import java.util.ArrayList;
import java.util.List;

class UndirectedGraphNode {
    public int label;
    public List<UndirectedGraphNode> neighbors;
    public UndirectedGraphNode(int x) {
        this.label = x;
        this.neighbors = new ArrayList<UndirectedGraphNode>();
    }
}
