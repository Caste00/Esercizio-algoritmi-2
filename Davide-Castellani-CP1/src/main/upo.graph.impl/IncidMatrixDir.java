import upo.graph.base.Edge;
import upo.graph.base.VisitResult;
import upo.graph.base.Graph;

import java.util.*;

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
        if (edge.getSource() >= numberOfVertices || edge.getTarget() >= numberOfVertices) {
            throw new IllegalArgumentException();
        }

        int edgeIndex = -1;

        for (int i = 0; i < numberOfEdge; i++) {
            if (matrix.get(edge.getSource()).get(i) == 1 && matrix.get(edge.getTarget()).get(i) == -1) {
                edgeIndex = i;
                break;
            }
        }

        if (edgeIndex == -1) {
            throw new NoSuchElementException();
        }

        for (ArrayList<Integer> row : matrix) {
            row.remove(edgeIndex);
        }

        numberOfEdge--;
    }

    @Override
    public Set<Integer> getAdjacent(Integer integer) throws NoSuchElementException {
        if (integer >= numberOfVertices || integer < 0)     throw new NoSuchElementException();

        Set<Integer> adjacent = new HashSet<>();

        for (int i = 0; i < numberOfEdge; i++) {
            if (matrix.get(integer).get(i) == 1) {
                for (int j = 0; j < numberOfVertices; j++) {
                    if (matrix.get(j).get(i) == -1) {
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
        if (integer >= numberOfVertices || integer1 >= numberOfVertices || integer < 0 || integer1 < 0)     throw new IllegalArgumentException();

        for (int i = 0; i < numberOfEdge; i++) {
            if (matrix.get(integer).get(i) == 1 && matrix.get(integer1).get(i) == -1) {
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

    private void initGraph(VisitResult visit) {
        for (Integer vertex : getVertices()) {
            visit.setColor(vertex, VisitResult.Color.WHITE);
        }
    }

    @Override
    public boolean isCyclic() {
        VisitResult result = new VisitResult(this);

        initGraph(result);

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
        return isDirected() && isCyclic();
    }

    @Override
    public VisitResult getBFSTree(Integer integer) throws UnsupportedOperationException, IllegalArgumentException {
        if (integer >= numberOfVertices || integer < 0)    throw new IllegalArgumentException();

        VisitResult result = new VisitResult(this);
        ArrayList<Integer> visit = new ArrayList<>();

        initGraph(result);
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
        if (integer >= numberOfVertices || integer < 0)     throw new IllegalArgumentException();

        VisitResult result = new VisitResult(this);
        int[] time = {0};

        initGraph(result);
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
        if (integer >= numberOfVertices || integer < 0)    throw new IllegalArgumentException();

        VisitResult result = new VisitResult(this);
        int[] time = {0};

        initGraph(result);

        visitDFS(integer, result, time);

        Integer next;
        while ((next = vertexColorControl(result)) != -1) {
            visitDFS(next, result, time);
        }

        return result;
    }

    private Integer vertexColorControl(VisitResult result) {
        for (int i = 0; i < numberOfVertices; i++) {
            if (result.getColor(i) != VisitResult.Color.BLACK ) {
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

        initGraph(result);

        while (!starts.isEmpty()) {
            if (starts.getFirst() >= numberOfVertices || starts.getFirst() < 0)  throw new IllegalArgumentException();
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

        ArrayList<ArrayList<Integer>> copy = new ArrayList<>();
        Integer[] result = new Integer[numberOfVertices];

        for (ArrayList<Integer> row : matrix) {
            copy.add(new ArrayList<>(row));
        }

        for (int i = 0; i < numberOfVertices; i++) {
            int choice = getVertexWithoutIncomingEdge(copy);
            removeOutcomoingEdge(choice, copy);
            result[i] = choice;
        }

        return result;
    }

    private int getVertexWithoutIncomingEdge(ArrayList<ArrayList<Integer>> copy) {
        for (int i = 0; i < copy.size(); i++) {
            boolean hasIncoming = false;
            for (int j = 0; j < numberOfEdge; j++) {
                if (copy.get(i).get(j) == -1 || copy.get(i).get(j) == -2) {
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

    private void removeOutcomoingEdge(Integer integer, ArrayList<ArrayList<Integer>> copy) {
        ArrayList<Integer> outcomingEdge = new ArrayList<>();

        for (int i = 0; i < numberOfEdge; i++) {
            if (copy.get(integer).get(i) == 1) {
                outcomingEdge.add(i);
            }
        }

        Collections.sort(outcomingEdge, Collections.reverseOrder());

        for (Integer edgeIndex : outcomingEdge) {
            for (int i = 0; i < copy.size(); i++) {
                if (copy.get(i).get(edgeIndex) == -1) {
                    copy.get(i).set(edgeIndex, 0);
                }
            }
        }

        for (int i = 0; i < numberOfEdge; i++) {
            copy.get(integer).set(i, -2);
        }
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
