package main.graph;

import java.util.HashMap;
import java.util.Map;

public class DiGraph<K, V> implements GraphADT<K, V> {
    private final Map<K, Vertex> adjVertices;

    public DiGraph() {
        this.adjVertices = new HashMap<>();
    }

    @Override
    public boolean removeEdge(K from, K to) {
        var source = adjVertices.get(from);
        var dest = adjVertices.get(to);
        if (source == null || dest == null)
            throw new RuntimeException("Edge does not exist");
        if (source.hasAdjVertex(dest)) {
            source.removeVertexTo(dest);
            return true;
        }
        return false;
    }

    @Override
    public Vertex addVertex(K key) {
        final Vertex vertex = new Vertex(key);
        adjVertices.putIfAbsent(key, vertex);
        return vertex;
    }

    @Override
    public void addEdge(V value, K from, K to) {
        Vertex source = adjVertices.get(from);
        Vertex dest = adjVertices.get(to);
        if (source == null || dest == null)
            throw new RuntimeException("Node does not exist");
        source.getVertices().putIfAbsent(dest, value);
    }

    @Override
    public Map<K, Vertex> getAdjVertices() {
        return adjVertices;
    }

    @Override
    public Vertex getVertex(K key) {
        return adjVertices.get(key);
    }

    @Override
    public Vertex removeVertex(K key) {
        Vertex vertex = adjVertices.get(key);
        if (vertex == null)
            throw new RuntimeException("Node does not exist");
        adjVertices.values().forEach(it -> {
            it.removeVertexTo(vertex);
        });
        adjVertices.remove(vertex.getKey());
        return vertex;
    }

    @Override
    public boolean contains(K key) {
        return adjVertices.containsKey(key);
    }

    public class Vertex {
        private final K key;
        private final Map<Vertex, V> vertices;
        private final Map<Vertex, Boolean> reversed;
        private boolean isVisited = false;
        private Double vacancy = 0.0;

        public Double getVacancy() {
            return vacancy;
        }

        public void setVacancy(Double vacancy) {
            this.vacancy = vacancy;
        }

        @Override
        public String toString() {
            return "Vertex{" +
                    "value=" + key +
                    '}';
        }

        public Vertex(K key) {
            this.key = key;
            this.vertices = new HashMap<>();
            this.reversed = new HashMap<>();
        }

        public Map<Vertex, V> getVertices() {
            return vertices;
        }

        public Vertex getEndVertex() {
            for (Map.Entry<Vertex, V> entry : vertices.entrySet()) {
                if (!reversed.containsKey(entry.getKey())) {
                    return entry.getKey();
                }
            }
            return null;
        }

        public K getKey() {
            return key;
        }

        public void setIsVisit(boolean isVisited) {
            this.isVisited = isVisited;
        }

        public boolean isVisited() {
            return isVisited;
        }

        public void addDistance(Vertex dest, V distance) {
            vertices.put(dest, distance);
        }

        public V getWeight(DiGraph<K, V>.Vertex dest) {
            return vertices.get(dest);
        }

        public boolean isReversed(DiGraph<K, V>.Vertex value) {
            return reversed.containsKey(value);
        }


        public void addReversedVertex(Vertex dest, Boolean reversed) {
            this.reversed.put(dest, reversed);
        }

        public Map<Vertex, Boolean> getReversed() {
            return reversed;
        }

        public Map<Vertex, Boolean> setReversedVerticesLength(V length) {
            for (Map.Entry<Vertex, Boolean> entry : reversed.entrySet()) {
                var key = entry.getKey();
                this.addDistance(key, length);
            }
            return reversed;
        }

        public void removeVertexTo(Vertex to) {
            vertices.remove(to);
        }

        public boolean hasAdjVertex(Vertex adjVertex) {
            return vertices.get(adjVertex) != null;
        }
    }

    public class Edge {
        private Vertex from;
        private Vertex to;
        private V value;

        public Edge(V value, Vertex from, Vertex to) {
            this.from = from;
            this.to = to;
            this.value = value;
        }

        public Vertex getFrom() {
            return from;
        }

        public Vertex getTo() {
            return to;
        }

        public V getValue() {
            return value;
        }
    }
}
