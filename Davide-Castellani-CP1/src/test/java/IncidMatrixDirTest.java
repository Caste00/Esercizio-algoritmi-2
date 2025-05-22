import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upo.graph.base.Edge;
import upo.graph.base.VisitResult;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
class IncidMatrixDirTest {
    static IncidMatrixDir matrice;

    private void loadDataCyclicGraph() {
        Edge edgeAB = Edge.getEdgeByVertexes(0, 1);
        Edge edgeCA = Edge.getEdgeByVertexes(2, 0);
        Edge edgeBC = Edge.getEdgeByVertexes(1, 2);
        Edge edgeCD = Edge.getEdgeByVertexes(2, 3);
        Edge edgeAD = Edge.getEdgeByVertexes(0, 3);

        matrice.addVertex();
        matrice.addVertex();
        matrice.addVertex();
        matrice.addVertex();

        matrice.addEdge(edgeAB);
        matrice.addEdge(edgeCA);
        matrice.addEdge(edgeBC);
        matrice.addEdge(edgeCD);
        matrice.addEdge(edgeAD);
    }

    private void loadDataGraph() {
        Edge edgeAB = Edge.getEdgeByVertexes(0, 1);
        Edge edgeCA = Edge.getEdgeByVertexes(0, 2);
        Edge edgeBC = Edge.getEdgeByVertexes(1, 2);
        Edge edgeCD = Edge.getEdgeByVertexes(2, 3);
        Edge edgeAD = Edge.getEdgeByVertexes(0, 3);

        matrice.addVertex();
        matrice.addVertex();
        matrice.addVertex();
        matrice.addVertex();

        matrice.addEdge(edgeAB);
        matrice.addEdge(edgeCA);
        matrice.addEdge(edgeBC);
        matrice.addEdge(edgeCD);
        matrice.addEdge(edgeAD);
    }

    @BeforeEach
    void init() {
        matrice = new IncidMatrixDir();
    }

    @Test
    void addVertex() {
        assertEquals(0, matrice.addVertex());
        assertEquals(1, matrice.addVertex());
        assertEquals(2, matrice.addVertex());
    }

    @Test
    void getVertices() {
        matrice.addVertex();
        matrice.addVertex();
        matrice.addVertex();

        assertEquals(Set.of(0, 1, 2), matrice.getVertices());
    }

    @Test
    void getEdges() {
        loadDataCyclicGraph();

        assertEquals(5, matrice.getEdges().size());
    }

    @Test
    void containsVertex() {
        assertFalse(matrice.containsVertex(2));

        matrice.addVertex();
        matrice.addVertex();

        assertTrue(matrice.containsVertex(2));
    }

    @Test
    void removeVertex() {
        matrice.addVertex();
        matrice.addVertex();
        matrice.addVertex();
        matrice.addVertex();

        matrice.removeVertex(1);
        assertEquals(Set.of(0, 1, 2), matrice.getVertices());
    }

    @Test
    void addEdge() {
        loadDataCyclicGraph();

        assertEquals(5, matrice.getEdges().size());
    }

    @Test
    void containsEdge() {
        Edge edgeAB = Edge.getEdgeByVertexes(0, 1);
        Edge edgeCA = Edge.getEdgeByVertexes(2, 0);
        Edge edgeBC = Edge.getEdgeByVertexes(1, 2);
        Edge edgeCD = Edge.getEdgeByVertexes(2, 3);

        matrice.addVertex();
        matrice.addVertex();
        matrice.addVertex();
        matrice.addVertex();

        matrice.addEdge(edgeAB);
        matrice.addEdge(edgeCA);
        matrice.addEdge(edgeBC);

        assertTrue(matrice.containsEdge(edgeAB));
        assertFalse(matrice.containsEdge(edgeCD));
    }

    @Test
    void removeEdge() {
        Edge edgeAB = Edge.getEdgeByVertexes(0, 1);
        Edge edgeAC = Edge.getEdgeByVertexes(0, 2);

        matrice.addVertex();
        matrice.addVertex();
        matrice.addVertex();

        matrice.addEdge(edgeAB);
        matrice.addEdge(edgeAC);

        assertTrue(matrice.containsEdge(edgeAC));

        matrice.removeEdge(edgeAC);

        assertFalse(matrice.containsEdge(edgeAC));
    }

    @Test
    void getAdjacent() {
        loadDataCyclicGraph();

        assertEquals(Set.of(1, 3), matrice.getAdjacent(0));
        assertEquals(Set.of(2), matrice.getAdjacent(1));
    }

    @Test
    void isAdjacent() {
        loadDataCyclicGraph();

        assertTrue(matrice.isAdjacent(0, 1));
        assertTrue(matrice.isAdjacent(2, 0));
        assertFalse(matrice.isAdjacent(0, 2));
    }

    @Test
    void size() {
        loadDataCyclicGraph();

        assertEquals(4, matrice.size());
    }

    @Test
    void isDirected() {
        assertTrue(matrice.isDirected());
    }

    @Test
    void isCyclic() {
        loadDataCyclicGraph();
        assertTrue(matrice.isCyclic());

        matrice.removeEdge(Edge.getEdgeByVertexes(1, 2));
        assertFalse(matrice.isCyclic());
    }

    @Test
    void isDAG() {
        loadDataCyclicGraph();
        assertTrue(matrice.isDAG());
    }

    @Test
    void getBFSTree() {
        loadDataCyclicGraph();
        VisitResult alberoVisite = matrice.getBFSTree(0);

        for (int i = 0; i < 4; i++) {
            assertEquals(VisitResult.Color.BLACK, alberoVisite.getColor(i));
            assertEquals(0, alberoVisite.getPartent(1));
        }
    }

    @Test
    void getDFSTree() {
        loadDataCyclicGraph();
        VisitResult alberoVisita = matrice.getDFSTree(2);

        for (int i = 0; i < 4; i++) {
            assertEquals(VisitResult.Color.BLACK, alberoVisita.getColor(i));
            assertEquals(0, alberoVisita.getPartent(1));
            assertEquals(Set.of(2), alberoVisita.getRoots());
        }
    }

    @Test
    void getDFSTOTForest() {
        loadDataCyclicGraph();
        matrice.addVertex();

        VisitResult forestaVisita = matrice.getDFSTOTForest(0);

        for (int i = 0; i < 5; i++) {
            assertEquals(VisitResult.Color.BLACK, forestaVisita.getColor(i));
            assertNull(forestaVisita.getPartent(4));
        }
    }

    @Test
    void testGetDFSTOTForest() {
        loadDataCyclicGraph();
        Edge edgeEF = Edge.getEdgeByVertexes(4, 5);
        matrice.addVertex();
        matrice.addVertex();

        matrice.addEdge(edgeEF);

        Integer[] integers = {2, 4};

        VisitResult forestaVisita = matrice.getDFSTOTForest(integers);

        for (int i = 0; i < matrice.size(); i++) {
            assertEquals(VisitResult.Color.BLACK, forestaVisita.getColor(i));
        }
        assertEquals(4, forestaVisita.getPartent(5));
    }

    @Test
    void topologicalSort() {
        loadDataGraph();

        Integer[] result = matrice.topologicalSort();
        Integer[] expectedResult = {0, 1, 2 ,3};

        assertArrayEquals(expectedResult, result);
    }

    @Test
    void stronglyConnectedComponents() {
    }

    @Test
    void connectedComponents() {
    }
}