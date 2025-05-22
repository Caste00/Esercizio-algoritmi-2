import upo.graph.base.Edge;
import upo.graph.base.VisitResult;
import upo.graph.base.WeightedGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class IncidMatrixDirWeight implements WeightedGraph {
    private int numberOfVertices;
    private int numberOfEdge;
    private ArrayList<ArrayList<Node>> matrix;

    public IncidMatrixDirWeight() {
        this.numberOfVertices = 0;
        this.numberOfEdge = 0;
        this.matrix = new ArrayList<>();
    }

    @Override
    public double getEdgeWeight(Edge edge) throws IllegalArgumentException, NoSuchElementException {
        if (edge.getSource() < 0 || edge.getSource() >= numberOfVertices || edge.getTarget() < 0 || edge.getTarget() >= numberOfVertices)
            throw new IllegalArgumentException();

        for (int i = 0; i < numberOfEdge; i++) {
            Node fromVal = matrix.get(edge.getSource()).get(i);
            Node toVal = matrix.get(edge.getTarget()).get(i);

            if (fromVal.getDirection() == Incidence.SOURCE && toVal.getDirection() == Incidence.TARGET) {
                return fromVal.getWeight();
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public void setEdgeWeight(Edge edge, double v) throws IllegalArgumentException, NoSuchElementException {
        if (edge.getSource() < 0 || edge.getSource() >= numberOfVertices || edge.getTarget() < 0 || edge.getTarget() >= numberOfVertices)
            throw new IllegalArgumentException();

        for (int i = 0; i < numberOfEdge; i++) {
            Node fromVal = matrix.get(edge.getSource()).get(i);
            Node toVal = matrix.get(edge.getTarget()).get(i);

            if (fromVal.getDirection() == Incidence.SOURCE && toVal.getDirection() == Incidence.TARGET) {
                fromVal.makeNode(Incidence.SOURCE, v);
                toVal.makeNode(Incidence.TARGET, v);
                return;
            }
        }

        throw new NoSuchElementException();
    }

    @Override
    public int addVertex() {
        ArrayList<Node> newVertex = new ArrayList<>();
        for (int i = 0; i < numberOfEdge; i++) {
            Node node = new Node();
            node.makeNode(Incidence.NONE, Node.INFINITY);
            newVertex.add(node);
        }
        matrix.add(newVertex);
        numberOfVertices++;

        return numberOfVertices - 1;
    }

    @Override
    public Set<Integer> getVertices() {
        Set<Integer> vertices = new HashSet<>();

        for (int i = 0; i < numberOfVertices; i++) {
            vertices.add(i);
        }

        return vertices;
    }

    @Override
    public Set<Edge> getEdges() {
        Set<Edge> edgeSet = new HashSet<>();

        for (int i = 0; i < numberOfEdge; i++) {
            int source = -1;
            int target = -1;

            for (int j = 0; j < numberOfVertices; j++) {
                Node node = matrix.get(j).get(i);

                if (node.getDirection() == Incidence.SOURCE) {
                    source = j;
                } else if (node.getDirection() == Incidence.TARGET) {
                    target = j;
                }
            }

            if (source != -1 && target != -1) {
                edgeSet.add(Edge.getEdgeByVertexes(source, target));
            }
        }

        return edgeSet;
    }

    @Override
    public boolean containsVertex(Integer integer) {
        return (integer < numberOfVertices);
    }

    @Override
    public void removeVertex(Integer integer) throws NoSuchElementException {
        if (numberOfVertices < integer) throw new NoSuchElementException();

        matrix.remove(integer);
        numberOfVertices--;
    }

    @Override
    public void addEdge(Edge edge) throws IllegalArgumentException {
        if (edge.getSource() < 0 || edge.getSource() >= numberOfVertices || edge.getTarget() < 0 || edge.getTarget() >= numberOfVertices)
            throw new IllegalArgumentException();

        int source = edge.getSource();
        int target = edge.getTarget();

        for (int i = 0; i < numberOfEdge; i++) {
            Node fromNode = matrix.get(source).get(i);
            Node toNode = matrix.get(target).get(i);

            if (fromNode.getDirection() == Incidence.SOURCE && toNode.getDirection() == Incidence.TARGET) {
                return;
            }
        }

        for (int i = 0; i < numberOfVertices; i++) {
            Node node = new Node();
            if (i == source) {
                node.makeNode(Incidence.SOURCE, Node.INFINITY);
            } else if (i == target) {
                node.makeNode(Incidence.TARGET, Node.INFINITY);
            } else {
                node.makeNode(Incidence.NONE, Node.INFINITY);
            }
            matrix.get(i).add(node);
        }

        numberOfEdge++;
    }

    @Override
    public boolean containsEdge(Edge edge) throws IllegalArgumentException {
        if (edge.getSource() < 0 || edge.getSource() >= numberOfVertices || edge.getTarget() < 0 || edge.getTarget() >= numberOfVertices)
            throw new IllegalArgumentException();

        int source = edge.getSource();
        int target = edge.getTarget();

        for (int i = 0; i < numberOfEdge; i++) {
            Incidence fromNode = (matrix.get(source).get(i)).getDirection();
            Incidence toNode = (matrix.get(target).get(i)).getDirection();

            if (fromNode == Incidence.SOURCE && toNode == Incidence.TARGET) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void removeEdge(Edge edge) throws IllegalArgumentException, NoSuchElementException {
        if (edge.getSource() < 0 || edge.getSource() >= numberOfVertices || edge.getTarget() < 0 || edge.getTarget() >= numberOfVertices)
            throw new IllegalArgumentException();

        int source = edge.getSource();
        int target = edge.getTarget();
        int col = -1;

        for (int i = 0; i < numberOfEdge; i++) {
            Node fromNode = matrix.get(source).get(i);
            Node toNode = matrix.get(target).get(i);

            if (fromNode.getDirection() == Incidence.SOURCE && toNode.getDirection() == Incidence.TARGET) {
                col = i;
                break;
            }
        }

        if (col == -1) {
            throw new NoSuchElementException();
        }

        for (int i = 0; i < numberOfVertices; i++) {
            matrix.get(i).remove(col);
        }

        numberOfEdge--;
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
        return numberOfVertices;
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

    // Non implementare
    @Override
    public WeightedGraph getBellmanFordShortestPaths(Integer integer) throws UnsupportedOperationException, IllegalArgumentException {
        return null;
    }

    // Non implementare
    @Override
    public WeightedGraph getDijkstraShortestPaths(Integer integer) throws UnsupportedOperationException, IllegalArgumentException {
        return null;
    }

    // Non implementare
    @Override
    public WeightedGraph getPrimMST(Integer integer) throws UnsupportedOperationException, IllegalArgumentException {
        return null;
    }

    // Non implementare
    @Override
    public WeightedGraph getKruskalMST() throws UnsupportedOperationException {
        return null;
    }

    // Non implementare
    @Override
    public WeightedGraph getFloydWarshallShortestPaths() throws UnsupportedOperationException {
        return null;
    }

}