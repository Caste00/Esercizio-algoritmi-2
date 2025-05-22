import upo.graph.base.Edge;
import upo.graph.base.VisitResult;
import upo.graph.base.Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/** Implementazione di una matrice di incidenza orientata */
public class IncidMatrixDir implements Graph {
    private int numberOfVertices;
    private int numberOfEdge;
    private ArrayList<ArrayList<Integer>> matrix;

    public IncidMatrixDir() {
        this.numberOfVertices = 0;
        this.numberOfEdge = 0;
        this.matrix = new ArrayList<>();
    }

    @Override
    public int addVertex() {
        ArrayList<Integer> newVertex = new ArrayList<>();
        //TODO: è brutto, migliorare!
        for (int i = 0; i < numberOfEdge; i++) {
            newVertex.add(0);
        }
        matrix.add(newVertex);
        numberOfVertices++;

        return numberOfVertices - 1;
    }

    @Override
    public Set<Integer> getVertices() {
        HashSet<Integer> vertices = new HashSet<>();

        for (int i = 0; i < numberOfVertices; i++) {
            vertices.add(i);
        }

        return vertices;
    }

    @Override
    public Set<Edge> getEdges() {
        Set<Edge> edges = new HashSet<>();

        for (int i = 0; i < numberOfEdge; i++) {
            Integer source = null;
            Integer target = null;

            for (int j = 0; j < numberOfVertices; j++) {
                int value = matrix.get(j).get(i);
                if (value == -1) {
                    source = j;
                } else if (value == 1) {
                    target = j;
                }
            }

            if (source != null && target != null) {
                edges.add(Edge.getEdgeByVertexes(source, target));
            }
        }

        return edges;
    }

    @Override
    public boolean containsVertex(Integer integer) {
        return numberOfVertices >= integer;
    }

    @Override
    public void removeVertex(Integer integer) throws NoSuchElementException {
        if (numberOfVertices < integer)     throw new NoSuchElementException();


        matrix.remove(integer);
        numberOfVertices--;
    }

    @Override
    public void addEdge(Edge edge) throws IllegalArgumentException {
        if (edge.getSource() >= numberOfVertices || edge.getTarget() >= numberOfVertices || edge.getSource() < 0 || edge.getTarget() < 0)     throw new IllegalArgumentException();
        boolean edgeAlreadyExists = false;

        for (int i = 0; i < numberOfEdge; i++) {
            if (matrix.get(edge.getSource()).get(i) == 1 && matrix.get(edge.getTarget()).get(i) == -1) {
                edgeAlreadyExists = true;
                break;
            }
        }

        if (!edgeAlreadyExists) {
            for (int i = 0; i < numberOfVertices; i++) {
                if (i == edge.getSource()) {
                    matrix.get(i).add(1);
                } else if (i == edge.getTarget()) {
                    matrix.get(i).add(-1);
                } else {
                    matrix.get(i).add(0);
                }
            }
            numberOfEdge++;
        }
    }

    @Override
    public boolean containsEdge(Edge edge) throws IllegalArgumentException {
        if (edge.getSource() >= numberOfVertices || edge.getTarget() >= numberOfVertices || edge.getSource() < 0 || edge.getTarget() < 0)     throw new IllegalArgumentException();

        for (int i = 0; i < numberOfEdge; i++) {
            if (matrix.get(edge.getSource()).get(i) == 1 && matrix.get(edge.getTarget()).get(i) == -1) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void removeEdge(Edge edge) throws IllegalArgumentException, NoSuchElementException {

    }

    @Override
    public Set<Integer> getAdjacent(Integer integer) throws NoSuchElementException {
        return Set.of();
    }

    @Override
    public boolean isAdjacent(Integer integer, Integer integer1) throws IllegalArgumentException {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public boolean isCyclic() {
        return false;
    }

    @Override
    public boolean isDAG() {
        return false;
    }

    @Override
    public VisitResult getBFSTree(Integer integer) throws UnsupportedOperationException, IllegalArgumentException {
        return null;
    }

    @Override
    public VisitResult getDFSTree(Integer integer) throws UnsupportedOperationException, IllegalArgumentException {
        return null;
    }

    @Override
    public VisitResult getDFSTOTForest(Integer integer) throws UnsupportedOperationException, IllegalArgumentException {
        return null;
    }

    @Override
    public VisitResult getDFSTOTForest(Integer[] integers) throws UnsupportedOperationException, IllegalArgumentException {
        return null;
    }

    @Override
    public Integer[] topologicalSort() throws UnsupportedOperationException {
        return new Integer[0];
    }

    @Override
    public Set<Set<Integer>> stronglyConnectedComponents() throws UnsupportedOperationException {
        return Set.of();
    }

    @Override
    public Set<Set<Integer>> connectedComponents() throws UnsupportedOperationException {
        return Set.of();
    }
}
