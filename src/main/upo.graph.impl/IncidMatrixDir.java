import upo.graph.base.Edge;
import upo.graph.base.VisitResult;
import upo.graph.base.Graph;

import java.util.*;

/** Implementazione di una matrice di incidenza orientata */
public class IncidMatrixDir implements Graph {
    private int numberOfVertices;
    private int numberOfEdge;
    private ArrayList<ArrayList<Incidence>> matrix;

    public IncidMatrixDir() {
        this.numberOfVertices = 0;
        this.numberOfEdge = 0;
        this.matrix = new ArrayList<>();
    }

    @Override
    public int addVertex() {
        ArrayList<Incidence> newVertex = new ArrayList<>();
        for (int i = 0; i < numberOfEdge; i++) {
            newVertex.add(Incidence.NONE);
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
                Incidence value = matrix.get(j).get(i);
                if (value == Incidence.SOURCE) {
                    source = j;
                } else if (value == Incidence.TARGET) {
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
        return numberOfVertices > integer;
    }

    @Override
    public void removeVertex(Integer integer) throws NoSuchElementException {
        if (numberOfVertices <= integer) throw new NoSuchElementException();
        matrix.remove(integer);
        numberOfVertices--;
    }

    @Override
    public void addEdge(Edge edge) throws IllegalArgumentException {
        if (edge.getSource() >= numberOfVertices || edge.getTarget() >= numberOfVertices || edge.getSource() < 0 || edge.getTarget() < 0)
            throw new IllegalArgumentException();

        boolean edgeAlreadyExists = false;

        for (int i = 0; i < numberOfEdge; i++) {
            if (matrix.get(edge.getSource()).get(i) == Incidence.SOURCE && matrix.get(edge.getTarget()).get(i) == Incidence.TARGET) {
                edgeAlreadyExists = true;
                break;
            }
        }

        if (!edgeAlreadyExists) {
            for (int i = 0; i < numberOfVertices; i++) {
                if (i == edge.getSource()) {
                    matrix.get(i).add(Incidence.SOURCE);
                } else if (i == edge.getTarget()) {
                    matrix.get(i).add(Incidence.TARGET);
                } else {
                    matrix.get(i).add(Incidence.NONE);
                }
            }
            numberOfEdge++;
        }
    }

    @Override
    public boolean containsEdge(Edge edge) throws IllegalArgumentException {
        if (edge.getSource() >= numberOfVertices || edge.getTarget() >= numberOfVertices || edge.getSource() < 0 || edge.getTarget() < 0)
            throw new IllegalArgumentException();

        for (int i = 0; i < numberOfEdge; i++) {
            if (matrix.get(edge.getSource()).get(i) == Incidence.SOURCE && matrix.get(edge.getTarget()).get(i) == Incidence.TARGET) {
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
            if (matrix.get(edge.getSource()).get(i) == Incidence.SOURCE && matrix.get(edge.getTarget()).get(i) == Incidence.TARGET) {
                edgeIndex = i;
                break;
            }
        }

        if (edgeIndex == -1) {
            throw new NoSuchElementException();
        }

        for (ArrayList<Incidence> row : matrix) {
            row.remove(edgeIndex);
        }

        numberOfEdge--;
    }

    @Override
    public Set<Integer> getAdjacent(Integer integer) throws NoSuchElementException {
        if (integer >= numberOfVertices || integer < 0) throw new NoSuchElementException();

        Set<Integer> adjacent = new HashSet<>();

        for (int i = 0; i < numberOfEdge; i++) {
            if (matrix.get(integer).get(i) == Incidence.SOURCE) {
                for (int j = 0; j < numberOfVertices; j++) {
                    if (matrix.get(j).get(i) == Incidence.TARGET) {
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
            if (matrix.get(integer).get(i) == Incidence.SOURCE && matrix.get(integer1).get(i) == Incidence.TARGET) {
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
        return isDirected() && !isCyclic();
    }

    @Override
    public VisitResult getBFSTree(Integer integer) throws UnsupportedOperationException, IllegalArgumentException {
        if (integer >= numberOfVertices || integer < 0) throw new IllegalArgumentException();
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
        if (integer >= numberOfVertices || integer < 0) throw new IllegalArgumentException();
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
        if (integer >= numberOfVertices || integer < 0) throw new IllegalArgumentException();
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
        initGraph(result);
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

        ArrayList<ArrayList<Incidence>> copy = new ArrayList<>();
        Integer[] result = new Integer[numberOfVertices];
        Set<Integer> visited = new HashSet<>();

        for (ArrayList<Incidence> row : matrix) {
            copy.add(new ArrayList<>(row));
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

    private int getVertexWithoutIncomingEdge(ArrayList<ArrayList<Incidence>> copy, Set<Integer> visited) {
        for (int i = 0; i < copy.size(); i++) {
            if (visited.contains(i)) continue;

            boolean hasIncoming = false;
            for (int j = 0; j < numberOfEdge; j++) {
                if (copy.get(i).get(j) == Incidence.TARGET) {
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

    private void removeOutcomingEdge(Integer integer, ArrayList<ArrayList<Incidence>> copy) {
        for (int i = 0; i < numberOfEdge; i++) {
            if (copy.get(integer).get(i) == Incidence.SOURCE) {
                for (ArrayList<Incidence> row : copy) {
                    row.set(i, Incidence.NONE);
                }
            }
        }
    }

    @Override
    public Set<Set<Integer>> stronglyConnectedComponents() throws UnsupportedOperationException {
        Set<Set<Integer>> cfc = new HashSet<>();
        ArrayList<Integer> resultDFS = new ArrayList<>();
        VisitResult result = new VisitResult(this);
        initGraph(result);
        for (int i = 0; i < numberOfVertices; i++) {
            if (result.getColor(i) == VisitResult.Color.WHITE) {
                normalDFS(i, result, resultDFS);
            }
        }

        IncidMatrixDir transposeGraph = getTranspose();
        VisitResult transposeResult = new VisitResult(transposeGraph);
        initGraph(transposeResult);
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

    private IncidMatrixDir getTranspose() {
        IncidMatrixDir transpose = new IncidMatrixDir();
        for (int i = 0; i < numberOfVertices; i++) {
            transpose.addVertex();
        }
        for (int i = 0; i < numberOfEdge; i++) {
            Integer source = null;
            Integer destination = null;
            for (int j = 0; j < numberOfVertices; j++) {
                if (matrix.get(j).get(i) == Incidence.SOURCE) {
                    source = j;
                } else if (matrix.get(j).get(i) == Incidence.TARGET) {
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
        return Set.of();
    }
}