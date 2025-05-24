import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upo.graph.base.Edge;
import upo.graph.base.VisitResult;

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

    private void loadDataGraph() {
        Edge edgeAB = Edge.getEdgeByVertexes(0, 1);
        Edge edgeCA = Edge.getEdgeByVertexes(0, 2);
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
        loadDataCyclicGraph();

        Set<Integer> result = matrix.getAdjacent(0);
        assertEquals(Set.of(1, 3), result);

        result = matrix.getAdjacent(3);
        assertEquals(Set.of(), result);
    }

    @Test
    void isAdjacent() {
        loadDataCyclicGraph();

        assertTrue(matrix.isAdjacent(0, 1));
        assertFalse(matrix.isAdjacent(3, 1));
    }

    @Test
    void size() {
        loadDataCyclicGraph();

        assertEquals(4, matrix.size());
    }

    @Test
    void isDirected() {
        assertTrue(matrix.isDirected());
    }

    @Test
    void isCyclic() {
        loadDataCyclicGraph();

        assertTrue(matrix.isCyclic());
    }

    @Test
    void isCyclicTest2() {
        loadDataGraph();

        assertFalse(matrix.isCyclic());
    }

    @Test
    void isDAG() {
        loadDataGraph();

        assertTrue(matrix.isDAG());
    }

    @Test
    void getBFSTree() {
        loadDataCyclicGraph();

        VisitResult alberoVisite = matrix.getBFSTree(0);

        for (int i = 0; i < 4; i++) {
            assertEquals(VisitResult.Color.BLACK, alberoVisite.getColor(i));
            assertEquals(0, alberoVisite.getPartent(1));
        }
    }

    @Test
    void getDFSTree() {
        loadDataCyclicGraph();
        VisitResult alberoVisita = matrix.getDFSTree(2);

        for (int i = 0; i < 4; i++) {
            assertEquals(VisitResult.Color.BLACK, alberoVisita.getColor(i));
            assertEquals(0, alberoVisita.getPartent(1));
            assertEquals(Set.of(2), alberoVisita.getRoots());
        }
    }

    @Test
    void getDFSTOTForest() {
        loadDataCyclicGraph();
        matrix.addVertex();

        VisitResult forestaVisita = matrix.getDFSTOTForest(0);

        for (int i = 0; i < 5; i++) {
            assertEquals(VisitResult.Color.BLACK, forestaVisita.getColor(i));
            assertNull(forestaVisita.getPartent(4));
        }
    }

    @Test
    void testGetDFSTOTForest() {
        loadDataCyclicGraph();
        Edge edgeEF = Edge.getEdgeByVertexes(4, 5);
        matrix.addVertex();
        matrix.addVertex();

        matrix.addEdge(edgeEF);

        Integer[] integers = {2, 4};

        VisitResult forestaVisita = matrix.getDFSTOTForest(integers);

        for (int i = 0; i < matrix.size(); i++) {
            assertEquals(VisitResult.Color.BLACK, forestaVisita.getColor(i));
        }
        assertEquals(4, forestaVisita.getPartent(5));
    }

    @Test
    void topologicalSort() {
        loadDataGraph();

        Integer[] result = matrix.topologicalSort();
        Integer[] expectedResult = {0, 1, 2 ,3};

        assertArrayEquals(expectedResult, result);
    }

    @Test
    void stronglyConnectedComponents() {
        loadDataCyclicGraph();

        Set<Set<Integer>> result = matrix.stronglyConnectedComponents();

        assertEquals(Set.of(Set.of(0, 1, 2), Set.of(3)), result);
    }

    @Test
    void stronglyConnectedComponentsTest2() {
        loadDataGraph();

        Set<Set<Integer>> result = matrix.stronglyConnectedComponents();

        assertEquals(Set.of(Set.of(0), Set.of(1), Set.of(2), Set.of(3)), result);
    }
}