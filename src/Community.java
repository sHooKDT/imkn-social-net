import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Community {
    List<Human> people = new ArrayList<>();
    List<Relation> relations = new ArrayList<>();

    public List<Relation> findRelations (Human x) {
        List<Relation> result = new ArrayList<>();
        for (Relation relation: relations) {
            if (relation.getFirst() == x || relation.getSecond() == x) {
                result.add(relation);
            }
        }
        return result;
    }

    public void addHuman(Human x) {
        people.add(x);
    }

    public void removeHuman (Human x) {
        people.remove(x);
        for (Relation rel: this.findRelations(x)) {
            this.removeRelation(rel);
        }
    }

    public void addRelation (Human first, Human second) {
        relations.add(new Relation(first, second));
    }

    public void removeRelation (Relation x) {
        relations.remove(x);
    }

    public String toString() {
        StringBuilder resultBuilder = new StringBuilder();

        resultBuilder.append("---PEOPLE---\n");
        for (Human x: people) resultBuilder.append(x.getName()).append('\n');

        resultBuilder.append("---RELATIONS---\n");
        for (Relation x: relations) {
            int f = people.indexOf(x.getFirst());
            int s = people.indexOf(x.getSecond());
            resultBuilder.append(f + " " + s).append('\n');
        }

        return resultBuilder.toString();
    }

    Community() {}

    Community (String[] lines) {
        boolean scanningRelations = false;

        for (String line: lines) {
            if (Objects.equals(line, "---PEOPLE---")) continue;
            if (Objects.equals(line, "---RELATIONS---")) {
                scanningRelations = true;
                continue;
            }

            if (!scanningRelations) {
                this.addHuman(new Human(line));
            } else {
                String[] parts = line.split(" ");
                Human first = people.get(Integer.parseInt(parts[0]));
                Human second = people.get(Integer.parseInt(parts[1]));
                this.addRelation(first, second);
            }
        }
    }
}
