import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Community vterakte = new Community();
        Human katya = new Human("Katya");
        Human senya = new Human("Senya");
        vterakte.addHuman(katya);
        vterakte.addHuman(senya);
        vterakte.addRelation(katya, senya);
        Community x = new Community(vterakte.toString().split("\n"));
    }

    public static Community loadFromFile (String path) {
//        return Files.readAllLines()
        return null;
    }
}
