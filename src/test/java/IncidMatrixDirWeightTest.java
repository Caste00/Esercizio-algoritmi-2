import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upo.graph.base.Edge;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class IncidMatrixDirWeightTest {
    static IncidMatrixDirWeight matrix;

    private void loadDataCyclicGraph() {
        Edge edgeAB = Edge.getEdgeByVertexes(0, 1);
        Edge edgeCA = Edge.getEdgeByVertexes(2, 0);
        Edge edgeBC = Edge.getEdgeByVertexes(1, 2);
        Edge edgeCD = Edge.getEdgeByVertexes(2, 3);
        Edge edgeAD = Edge.getEdgeByVertexes(0, 3);

        matrix.addVertex();
        matrix.addVertex();
        matrix.addVertex();
        matrix.addVertex();

        matrix.addEdge(edgeAB);
        matrix.addEdge(edgeCA);
        matrix.addEdge(edgeBC);
        matrix.addEdge(edgeCD);
        matrix.addEdge(edgeAD);

        matrix.setEdgeWeight(edgeAB, 2);
        matrix.setEdgeWeight(edgeCA, 3);
        matrix.setEdgeWeight(edgeBC, 5);
        matrix.setEdgeWeight(edgeCD, 7);
        matrix.setEdgeWeight(edgeAD, 1);
    }

    @BeforeEach
    void init() {
        matrix = new IncidMatrixDirWeight();
    }

    @Test
    void getEdgeWeight() {
        loadDataCyclicGraph();
        Edge edgeAB = Edge.getEdgeByVertexes(0, 1);

        assertEquals(2D, matrix.getEdgeWeight(edgeAB));
    }

    @Test
    void setEdgeWeight() {
        Edge AB = Edge.getEdgeByVertexes(0, 1);
        matrix.addVertex();
        matrix.addVertex();
        matrix.addEdge(AB);

        matrix.setEdgeWeight(AB, 5);
        assertEquals(5D, matrix.getEdgeWeight(AB));

        matrix.setEdgeWeight(AB, 1);
        assertEquals(1D, matrix.getEdgeWeight(AB));
    }

    @Test
    void addVertex() {
        matrix.addVertex();
        matrix.addVertex();

        assertEquals(2, matrix.size());
    }

    @Test
    void getVertices() {
        loadDataCyclicGraph();

        Set<Integer> result = matrix.getVertices();
        assertEquals(Set.of(0, 1, 2, 3), result);
    }

    @Test
    void getEdges() {
        loadDataCyclicGraph();
        Edge edgeAB = Edge.getEdgeByVertexes(0, 1);
        Edge edgeCA = Edge.getEdgeByVertexes(2, 0);
        Edge edgeBC = Edge.getEdgeByVertexes(1, 2);
        Edge edgeCD = Edge.getEdgeByVertexes(2, 3);
        Edge edgeAD = Edge.getEdgeByVertexes(0, 3);

        Set<Edge> result = matrix.getEdges();
        Set<Edge> setEdge = new HashSet<>();
        setEdge.add(edgeAB);
        setEdge.add(edgeCA);
        setEdge.add(edgeBC);
        setEdge.add(edgeCD);
        setEdge.add(edgeAD);

        assertEquals(setEdge, result);
    }

    @Test
    void containsVertex() {
        loadDataCyclicGraph();

        assertTrue(matrix.containsVertex(0));
        assertTrue(matrix.containsVertex(1));
        assertTrue(matrix.containsVertex(2));
        assertTrue(matrix.containsVertex(3));
        assertFalse(matrix.containsVertex(4));
    }

    @Test
    void removeVertex() {
        loadDataCyclicGraph();

        matrix.removeVertex(1);
        assertEquals(Set.of(0, 1, 2), matrix.getVertices());
    }

    @Test
    void addEdge() {
        matrix.addVertex();
        matrix.addVertex();

        Edge edge = Edge.getEdgeByVertexes(0, 1);
        matrix.addEdge(edge);

        Set<Edge> result = matrix.getEdges();
        assertTrue(result.contains(edge));
    }

    @Test
    void containsEdge() {
        loadDataCyclicGraph();

        Edge edgeAB = Edge.getEdgeByVertexes(0, 1);
        Edge edgeDB = Edge.getEdgeByVertexes(3, 1);

        assertTrue(matrix.containsEdge(edgeAB));
        assertFalse(matrix.containsEdge(edgeDB));
    }

    @Test
    void removeEdge() {
        loadDataCyclicGraph();

        Edge edgeAB = Edge.getEdgeByVertexes(0, 1);
        matrix.removeEdge(edgeAB);

        Set<Edge> result = matrix.getEdges();
        assertEquals(4, result.size());
    }

    @Test
    void getAdjacent() {
    }

    @Test
    void isAdjacent() {
    }

    @Test
    void size() {
    }

    @Test
    void isDirected() {
    }

    @Test
    void isCyclic() {
    }

    @Test
    void isDAG() {
    }

    @Test
    void getBFSTree() {
    }

    @Test
    void getDFSTree() {
    }

    @Test
    void getDFSTOTForest() {
    }

    @Test
    void testGetDFSTOTForest() {
    }

    @Test
    void topologicalSort() {
    }

    @Test
    void stronglyConnectedComponents() {
    }

    @Test
    void connectedComponents() {
    }
}