public class Node {
    private Incidence direction;
    private Double weight;
    public static final double INFINITY = Double.POSITIVE_INFINITY;

    public Node() {
        this.direction = Incidence.NONE;
        this.weight = INFINITY;
    }

    public void makeNode(Incidence dir, Double v) {
        this.direction = dir;
        this.weight = v;
    }

    public void putDirection(Incidence dir) {
        direction = dir;
    }

    public void pudWeight(Double v) {
        weight = v;
    }

    public Incidence getDirection() {
        return direction;
    }

    public Double getWeight() {
        return weight;
    }
}