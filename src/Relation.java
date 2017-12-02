public class Relation {
    private Human first;
    private Human second;

    public Relation(Human first, Human second) {
        this.first = first;
        this.second = second;
    }

    public Human getFirst() {
        return first;
    }

    public Human getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return String.format("%s + %s", getFirst(), getSecond());
    }
}
