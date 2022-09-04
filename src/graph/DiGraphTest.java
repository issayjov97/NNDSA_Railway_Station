package graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DiGraphTest {

    private GraphADT<String, Double> graph;

    @BeforeEach
    void setUp() {
        graph = new DiGraph<String, Double>();
        final var A = graph.addVertex("A");
        final var B = graph.addVertex("B");
        final var C = graph.addVertex("C");
        final var D = graph.addVertex("D");
        final var E = graph.addVertex("E");
        final var F = graph.addVertex("F");

        A.addDistance(B,10.00);
        A.addDistance(C,15.00);

        B.addDistance(F,15.00);
        B.addDistance(D,12.00);

        C.addDistance(E,10.00);

        D.addDistance(F,1.00);
        D.addDistance(E,2.00);

        F.addDistance(E,5.00);

    }

    @Test
    void removeEdge() {
    }

    @Test
    void addVertex() {
    }

    @Test
    void addEdge() {
    }

    @Test
    void removeVertex() {
    }

    @Test
    void contains() {
    }

    @Test
    void dijkstraShortestPath() {
    }
}