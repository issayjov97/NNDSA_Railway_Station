package service;

import graph.DiGraph;
import graph.GraphADT;

import java.util.HashMap;
import java.util.Map;

public class DijkstraSPAlgorithm {

    private final GraphADT<String, Double> graph;
    private Double shortestPathCost = 0.0;
    private String path = "";

    public DijkstraSPAlgorithm(GraphADT<String, Double> graph) {
        this.graph = graph;
    }

    public String getPath() {
        return path;
    }

    public Map<DiGraph<String, Double>.Vertex, DiGraph<String, Double>.Vertex> DijkstraShortestPath(
            DiGraph<String, Double>.Vertex startVertex,
            DiGraph<String, Double>.Vertex endVertex,
            Double length
    ) {
        if(length > endVertex.getVacancy())
            throw new RuntimeException("End vertex is full");

        Map<DiGraph<String, Double>.Vertex, DiGraph<String, Double>.Vertex> changeAt = new HashMap<>();
        Map<DiGraph<String, Double>.Vertex, Double> shortestPathMap = new HashMap<>();
        Map<DiGraph<String, Double>.Vertex, DiGraph<String, Double>.Vertex> shortestPath = new HashMap<>();
        DiGraph<String, Double>.Vertex prevNode;

        changeAt.put(startVertex, null);


        for (DiGraph<String, Double>.Vertex vertex : graph.getAdjVertices().values()) {
            if (vertex.getKey().equals(startVertex.getKey()))
                shortestPathMap.put(startVertex, 0.0);
            else
                shortestPathMap.put(vertex, Double.POSITIVE_INFINITY);
        }

        for (Map.Entry<DiGraph<String, Double>.Vertex, Double> entry : startVertex.getVertices().entrySet()) {
            shortestPathMap.put(entry.getKey(), Double.parseDouble(String.valueOf(entry.getValue())));
            changeAt.put(entry.getKey(), startVertex);
        }

        startVertex.setIsVisit(true);
        prevNode = startVertex;
        while (true) {
            DiGraph<String, Double>.Vertex currentNode = closestReachableUnvisited(shortestPathMap,prevNode, length);

            if (currentNode == null) {
                System.out.println(shortestPathMap);
                System.out.println("There isn't a path between " + startVertex.getKey() + " and " + endVertex.getKey());
                return null;
            }
            if (currentNode.getKey().equals(endVertex.getKey())) {
                DiGraph<String, Double>.Vertex child = endVertex;
                String path = endVertex.getKey();

                while (true) {
                    var parent = changeAt.get(child);
                    if (parent == null)
                        break;
                    path = parent.getKey() + " -> " + path;
                    shortestPath.put(child, parent);
                    child = parent;

                }
                this.path = path;
                this.shortestPathCost = shortestPathMap.get(endVertex);
                return shortestPath;
            }
            currentNode.setIsVisit(true);

            for (Map.Entry<DiGraph<String, Double>.Vertex, Double> entry : currentNode.getVertices().entrySet()) {
                if (entry.getKey().isVisited())
                    continue;

                if (shortestPathMap.get(currentNode) + entry.getValue() < shortestPathMap.get(endVertex)) {
                    shortestPathMap.put(entry.getKey(), shortestPathMap.get(currentNode) + entry.getValue());
                    changeAt.put(entry.getKey(), currentNode);
                }
            }
            prevNode = currentNode;
        }
    }

    public void reset() {
        for (DiGraph<String, Double>.Vertex node : graph.getAdjVertices().values()) {
            node.setIsVisit(false);
        }
    }

    private DiGraph<String, Double>.Vertex closestReachableUnvisited(Map<DiGraph<String, Double>.Vertex, Double> shortestPathMap,DiGraph<String, Double>.Vertex prevNode, Double trainLength) {
        double shortestDistance = Double.POSITIVE_INFINITY;
        DiGraph<String, Double>.Vertex closestReachableNode = null;
        for (DiGraph<String, Double>.Vertex node : graph.getAdjVertices().values()) {
            if (node.isVisited())
                continue;
            double currentDistance = shortestPathMap.get(node);
            if (currentDistance == Double.POSITIVE_INFINITY)
                continue;
//            if (!prevNode.isReversed(node) && prevNode.getWeight(node) > node.getVacancy() )
//                continue;
            if (currentDistance < shortestDistance) {
                shortestDistance = currentDistance;
                closestReachableNode = node;
            }
        }
        return closestReachableNode;
    }

    public Double getShortestPathCost() {
        return shortestPathCost;
    }
}
