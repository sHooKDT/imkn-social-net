import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SocialGuiController {
    @FXML private TitledPane peopleTab;
    @FXML private TitledPane relationsTab;

    @FXML private Button magicBtn;

    @FXML private ListView<Human> peopleList;
    @FXML private ListView<Relation> relationsList;

    private Community vterakte;
    private String db_filepath = "/home/shook/Downloads/social_db.txt";

    private void updateData() {
        ObservableList<Human> people = FXCollections.observableArrayList(vterakte.people);
        peopleList.setItems(people);

        ObservableList<Relation> relations = FXCollections.observableArrayList(vterakte.relations);
        relationsList.setItems(relations);
    }

    public void initialize() {
        peopleList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Path db = Paths.get(db_filepath);

        if (Files.exists(db)) try {
            String[] read = Files.readAllLines(db).toArray(new String[0]);
            vterakte = new Community(read);
        } catch (IOException e) {
            vterakte = new Community();
        } else vterakte = new Community();

        updateData();
    }

    public void saveHandler() {
        try (PrintWriter writer = new PrintWriter(db_filepath)) {
            writer.print(vterakte.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeHandler() {
        if (!peopleList.getSelectionModel().isEmpty()) {
            for (Human selected: peopleList.getSelectionModel().getSelectedItems()) {
                vterakte.removeHuman(selected);
            }
        }
        if (!relationsList.getSelectionModel().isEmpty()) {
            for (Relation selected: relationsList.getSelectionModel().getSelectedItems()) {
                vterakte.removeRelation(selected);
            }
        }

        updateData();
    }

    public void magicHandler() {
        if (!peopleList.getSelectionModel().isEmpty()) {
            Human selected = peopleList.getSelectionModel().getSelectedItem();
            relationsList.setItems(FXCollections.observableArrayList(vterakte.findRelations(selected)));
            relationsTab.setExpanded(true);
        }
        magicBtn.setText("Unmagic");
    }

    public void relateHandler() {
        if (!peopleList.getSelectionModel().isEmpty() && peopleList.getSelectionModel().getSelectedItems().size() == 2) {
            vterakte.addRelation(peopleList.getSelectionModel().getSelectedItems().get(0), peopleList.getSelectionModel().getSelectedItems().get(1));
        }

        updateData();
    }
}
