import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upo.graph.base.Edge;
import upo.graph.base.VisitResult;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
class IncidMatrixDirTest {
    static IncidMatrixDir matrix;

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
        matrix = new IncidMatrixDir();
    }

    @Test
    void addVertex() {
        assertEquals(0, matrix.addVertex());
        assertEquals(1, matrix.addVertex());
        assertEquals(2, matrix.addVertex());
    }

    @Test
    void getVertices() {
        matrix.addVertex();
        matrix.addVertex();
        matrix.addVertex();

        assertEquals(Set.of(0, 1, 2), matrix.getVertices());
    }

    @Test
    void getEdges() {
        loadDataCyclicGraph();

        assertEquals(5, matrix.getEdges().size());
    }

    @Test
    void containsVertex() {
        assertFalse(matrix.containsVertex(2));

        matrix.addVertex();
        matrix.addVertex();
        matrix.addVertex();

        assertTrue(matrix.containsVertex(2));
    }

    @Test
    void removeVertex() {
        matrix.addVertex();
        matrix.addVertex();
        matrix.addVertex();
        matrix.addVertex();

        matrix.removeVertex(1);
        assertEquals(Set.of(0, 1, 2), matrix.getVertices());
    }

    @Test
    void addEdge() {
        loadDataCyclicGraph();

        assertEquals(5, matrix.getEdges().size());
    }

    @Test
    void containsEdge() {
        Edge edgeAB = Edge.getEdgeByVertexes(0, 1);
        Edge edgeCA = Edge.getEdgeByVertexes(2, 0);
        Edge edgeBC = Edge.getEdgeByVertexes(1, 2);
        Edge edgeCD = Edge.getEdgeByVertexes(2, 3);

        matrix.addVertex();
        matrix.addVertex();
        matrix.addVertex();
        matrix.addVertex();

        matrix.addEdge(edgeAB);
        matrix.addEdge(edgeCA);
        matrix.addEdge(edgeBC);

        assertTrue(matrix.containsEdge(edgeAB));
        assertFalse(matrix.containsEdge(edgeCD));
    }

    @Test
    void removeEdge() {
        Edge edgeAB = Edge.getEdgeByVertexes(0, 1);
        Edge edgeAC = Edge.getEdgeByVertexes(0, 2);

        matrix.addVertex();
        matrix.addVertex();
        matrix.addVertex();

        matrix.addEdge(edgeAB);
        matrix.addEdge(edgeAC);

        assertTrue(matrix.containsEdge(edgeAC));

        matrix.removeEdge(edgeAC);

        assertFalse(matrix.containsEdge(edgeAC));
    }

    @Test
    void getAdjacent() {
        loadDataCyclicGraph();

        assertEquals(Set.of(1, 3), matrix.getAdjacent(0));
        assertEquals(Set.of(2), matrix.getAdjacent(1));
    }

    @Test
    void isAdjacent() {
        loadDataCyclicGraph();

        assertTrue(matrix.isAdjacent(0, 1));
        assertTrue(matrix.isAdjacent(2, 0));
        assertFalse(matrix.isAdjacent(0, 2));
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

        matrix.removeEdge(Edge.getEdgeByVertexes(1, 2));
        assertFalse(matrix.isCyclic());
    }

    @Test
    void isDAG() {
        loadDataCyclicGraph();
        assertFalse(matrix.isDAG());
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
}