package graph;

import java.util.Map;

public interface GraphADT<K, V> {
    Map<K, DiGraph<K,V>.Vertex> getAdjVertices();

    boolean removeEdge(K from, K to);

    DiGraph<K,V>.Vertex addVertex(K key);

    void addEdge(V value, K from, K to);

    DiGraph<K,V>.Vertex removeVertex(K key);

    boolean contains(K key);

    DiGraph<K,V>.Vertex getVertex(K key);

}
