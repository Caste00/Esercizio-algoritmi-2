public enum Incidence {
    NONE(0),
    SOURCE(-1),
    TARGET(1);

    private final int value;

    Incidence(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}