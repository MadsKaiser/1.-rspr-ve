package com.example.agrisys;

import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
// Mads
public class GraphStorage {
    private static final GraphStorage instance = new GraphStorage();
    private final List<Node> graphs = new ArrayList<>();

    private GraphStorage() {}

    public static GraphStorage getInstance() {
        return instance;
    }

    public void addGraph(Node graph) {
        graphs.add(graph);
    }

    public List<Node> getGraphs() {
        return new ArrayList<>(graphs);
    }

    public void clearGraphs() {
        graphs.clear();
    }
}