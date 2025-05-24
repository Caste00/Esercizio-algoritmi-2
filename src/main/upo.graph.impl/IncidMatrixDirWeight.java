import upo.graph.base.Edge;
import upo.graph.base.VisitResult;
import upo.graph.base.WeightedGraph;

import java.util.*;

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
        if (integer >= numberOfVertices || integer < 0) throw new NoSuchElementException();

        Set<Integer> adjacent = new HashSet<>();

        for (int i = 0; i < numberOfEdge; i++) {
            if (matrix.get(integer).get(i).getDirection() == Incidence.SOURCE) {
                for (int j = 0; j < numberOfVertices; j++) {
                    if (matrix.get(j).get(i).getDirection() == Incidence.TARGET) {
                        adjacent.add(j);
                        break;
                    }
                }
            }
        }

        return adjacent;
    }

    @Override
    public boolean isAdjacent(Integer integer, Integer integer1) throws IllegalArgumentException {
        if (integer >= numberOfVertices || integer1 >= numberOfVertices || integer < 0 || integer1 < 0)
            throw new IllegalArgumentException();

        for (int i = 0; i < numberOfEdge; i++) {
            if (matrix.get(integer).get(i).getDirection() == Incidence.SOURCE && matrix.get(integer1).get(i).getDirection() == Incidence.TARGET) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int size() {
        return numberOfVertices;
    }

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public boolean isCyclic() {
        VisitResult result = new VisitResult(this);
        for (Integer vertex : getVertices()) {
            if (result.getColor(vertex) == VisitResult.Color.WHITE) {
                if (visitDFSIsCyclic(vertex, result)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean visitDFSIsCyclic(Integer integer, VisitResult result) {
        result.setColor(integer, VisitResult.Color.GRAY);
        for (Integer adjacent : getAdjacent(integer)) {
            VisitResult.Color color = result.getColor(adjacent);
            if (color == VisitResult.Color.WHITE) {
                if (visitDFSIsCyclic(adjacent, result)) {
                    return true;
                }
            } else if (color == VisitResult.Color.GRAY) {
                return true;
            }
        }
        result.setColor(integer, VisitResult.Color.BLACK);
        return false;
    }

    @Override
    public boolean isDAG() {
        return isDirected() && !isCyclic();
    }

    @Override
    public VisitResult getBFSTree(Integer integer) throws UnsupportedOperationException, IllegalArgumentException {
        if (integer >= numberOfVertices || integer < 0) throw new IllegalArgumentException();
        VisitResult result = new VisitResult(this);
        ArrayList<Integer> visit = new ArrayList<>();
        visitBFS(integer, result, visit);
        return result;
    }

    private void visitBFS(Integer integer, VisitResult result, ArrayList<Integer> visita) {
        result.setColor(integer, VisitResult.Color.GRAY);
        visita.add(integer);

        while (!visita.isEmpty()) {
            for (Integer adjacent : getAdjacent(visita.getFirst())) {
                if (result.getColor(adjacent) == VisitResult.Color.WHITE) {
                    result.setColor(adjacent, VisitResult.Color.GRAY);
                    result.setParent(adjacent, visita.getFirst());
                    visita.add(adjacent);
                }
            }
            result.setColor(visita.getFirst(), VisitResult.Color.BLACK);
            visita.removeFirst();
        }
    }

    @Override
    public VisitResult getDFSTree(Integer integer) throws UnsupportedOperationException, IllegalArgumentException {
        if (integer >= numberOfVertices || integer < 0) throw new IllegalArgumentException();
        VisitResult result = new VisitResult(this);
        int[] time = {0};
        visitDFS(integer, result, time);
        return result;
    }

    private void visitDFS(Integer integer, VisitResult result, int[] time) {
        result.setColor(integer, VisitResult.Color.GRAY);
        result.setStartTime(integer, ++time[0]);

        for (Integer adjacent : getAdjacent(integer)) {
            if (result.getColor(adjacent) == VisitResult.Color.WHITE) {
                result.setParent(adjacent, integer);
                visitDFS(adjacent, result, time);
            }
        }

        result.setColor(integer, VisitResult.Color.BLACK);
        result.setEndTime(integer, ++time[0]);
    }

    @Override
    public VisitResult getDFSTOTForest(Integer integer) throws UnsupportedOperationException, IllegalArgumentException {
        if (integer >= numberOfVertices || integer < 0) throw new IllegalArgumentException();
        VisitResult result = new VisitResult(this);
        int[] time = {0};
        visitDFS(integer, result, time);
        Integer next;
        while ((next = vertexColorControl(result)) != -1) {
            visitDFS(next, result, time);
        }
        return result;
    }

    private Integer vertexColorControl(VisitResult result) {
        for (int i = 0; i < numberOfVertices; i++) {
            if (result.getColor(i) != VisitResult.Color.BLACK) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public VisitResult getDFSTOTForest(Integer[] integers) throws UnsupportedOperationException, IllegalArgumentException {
        VisitResult result = new VisitResult(this);
        int[] time = {0};
        ArrayList<Integer> starts = new ArrayList<>(Arrays.asList(integers));
        while (!starts.isEmpty()) {
            if (starts.getFirst() >= numberOfVertices || starts.getFirst() < 0) throw new IllegalArgumentException();
            if (result.getColor(starts.getFirst()) == VisitResult.Color.WHITE) {
                visitDFS(starts.getFirst(), result, time);
            }
            starts.removeFirst();
        }
        Integer next;
        while ((next = vertexColorControl(result)) != -1) {
            visitDFS(next, result, time);
        }
        return result;
    }

    @Override
    public Integer[] topologicalSort() throws UnsupportedOperationException {
        if (this.isCyclic()) throw new UnsupportedOperationException();

        ArrayList<ArrayList<Node>> copy = new ArrayList<>();
        Integer[] result = new Integer[numberOfVertices];
        Set<Integer> visited = new HashSet<>();

        for (ArrayList<Node> row : matrix) {
            ArrayList<Node> newRow = new ArrayList<>();
            for (Node node : row) {
                Node newNode = new Node();
                newNode.makeNode(node.getDirection(), node.getWeight());
                newRow.add(newNode);
            }
            copy.add(newRow);
        }

        for (int i = 0; i < numberOfVertices; i++) {
            int choice = getVertexWithoutIncomingEdge(copy, visited);
            if (choice == -1) throw new UnsupportedOperationException();
            visited.add(choice);
            result[i] = choice;
            removeOutcomingEdge(choice, copy);
        }

        return result;
    }

    private int getVertexWithoutIncomingEdge(ArrayList<ArrayList<Node>> copy, Set<Integer> visited) {
        for (int i = 0; i < copy.size(); i++) {
            if (visited.contains(i)) continue;

            boolean hasIncoming = false;
            for (int j = 0; j < numberOfEdge; j++) {
                if (copy.get(i).get(j).getDirection() == Incidence.TARGET) {
                    hasIncoming = true;
                    break;
                }
            }

            if (!hasIncoming) {
                return i;
            }
        }
        return -1;
    }

    private void removeOutcomingEdge(Integer integer, ArrayList<ArrayList<Node>> copy) {
        for (int i = 0; i < numberOfEdge; i++) {
            if (copy.get(integer).get(i).getDirection() == Incidence.SOURCE) {
                for (int j = 0; j < numberOfVertices; j++) {
                    copy.get(j).get(i).putDirection(Incidence.NONE);
                    copy.get(j).get(i).putWeight(Node.INFINITY);
                }
            }
        }
    }

    @Override
    public Set<Set<Integer>> stronglyConnectedComponents() throws UnsupportedOperationException {
        Set<Set<Integer>> cfc = new HashSet<>();
        ArrayList<Integer> resultDFS = new ArrayList<>();
        VisitResult result = new VisitResult(this);
        for (int i = 0; i < numberOfVertices; i++) {
            if (result.getColor(i) == VisitResult.Color.WHITE) {
                normalDFS(i, result, resultDFS);
            }
        }

        IncidMatrixDirWeight transposeGraph = getTranspose();
        VisitResult transposeResult = new VisitResult(transposeGraph);
        Collections.reverse(resultDFS);
        for (Integer vertex : resultDFS) {
            if (transposeResult.getColor(vertex) == VisitResult.Color.WHITE) {
                Set<Integer> component = new HashSet<>();
                transposeGraph.dfsComponent(vertex, transposeResult, component);
                cfc.add(component);
            }
        }

        return cfc;
    }

    private void normalDFS(Integer integer, VisitResult result, ArrayList<Integer> resultDFS) {
        result.setColor(integer, VisitResult.Color.GRAY);
        for (Integer adjacent : getAdjacent(integer)) {
            if (result.getColor(adjacent) == VisitResult.Color.WHITE) {
                normalDFS(adjacent, result, resultDFS);
            }
        }
        result.setColor(integer, VisitResult.Color.BLACK);
        resultDFS.add(integer);
    }

    private IncidMatrixDirWeight getTranspose() {
        IncidMatrixDirWeight transpose = new IncidMatrixDirWeight();
        for (int i = 0; i < numberOfVertices; i++) {
            transpose.addVertex();
        }
        for (int i = 0; i < numberOfEdge; i++) {
            Integer source = null;
            Integer destination = null;
            for (int j = 0; j < numberOfVertices; j++) {
                if (matrix.get(j).get(i).getDirection() == Incidence.SOURCE) {
                    source = j;
                } else if (matrix.get(j).get(i).getDirection() == Incidence.TARGET) {
                    destination = j;
                }
            }
            if (source != null && destination != null) {
                transpose.addEdge(Edge.getEdgeByVertexes(destination, source));
            }
        }
        return transpose;
    }

    private void dfsComponent(Integer integer, VisitResult result, Set<Integer> component) {
        result.setColor(integer, VisitResult.Color.GRAY);
        component.add(integer);
        for (Integer adjacent : getAdjacent(integer)) {
            if (result.getColor(adjacent) == VisitResult.Color.WHITE) {
                dfsComponent(adjacent, result, component);
            }
        }
        result.setColor(integer, VisitResult.Color.BLACK);
    }

    @Override
    public Set<Set<Integer>> connectedComponents() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
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