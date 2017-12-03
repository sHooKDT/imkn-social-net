import com.sun.tools.javac.util.ArrayUtils;

import java.util.*;

public class Community {
    List<Human> people = new ArrayList<>();
    List<Relation> relations = new ArrayList<>();

    public final int RECOMMENDATIONS_LIMIT = 10;

    public List<Relation> findRelations (Human x) {
        List<Relation> result = new ArrayList<>();
        for (Relation relation: relations) {
            if (relation.getFirst() == x || relation.getSecond() == x) {
                result.add(relation);
            }
        }
        return result;
    }

    public List<Human> findRelated (Human x) {
        List<Human> result = new ArrayList<>();

        for (Relation relation: findRelations(x)) {
            if (relation.getFirst() == x) result.add(relation.getSecond());
            else result.add(relation.getFirst());
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

    public Human findMostFriendly () {
        int[] humanFriendCounters = new int[people.size()];

        for (Relation x: relations) {
            humanFriendCounters[people.indexOf(x.getFirst())]++;
            humanFriendCounters[people.indexOf(x.getSecond())]++;
        }

        int maxIndex=0;
        int maxValue=0;

        for (int i=0; i < humanFriendCounters.length; i++) {
            if (humanFriendCounters[i] > maxValue) {
                maxValue = humanFriendCounters[i];
                maxIndex = i;
            };
        }

        return people.get(maxIndex);
    }

    public List<Human> findRecommendations (Human x) {
        List<Human> recommendations = new ArrayList<>(20);

        Queue<Human> bfsQueue = new LinkedList<>();
        bfsQueue.add(x);

        while (!bfsQueue.isEmpty() && recommendations.size() < RECOMMENDATIONS_LIMIT) {
            for (Human rec: findRelated(bfsQueue.poll())) {
                if (!recommendations.contains(rec) && rec != x) {
                    bfsQueue.add(rec);
                    recommendations.add(rec);
                }

                if (recommendations.size() >= RECOMMENDATIONS_LIMIT) break;
            }
        }

        return recommendations;
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
