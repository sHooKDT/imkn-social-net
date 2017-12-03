import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SocialGuiController {

    @FXML private ListView<Human> peopleList;
    @FXML private ListView<Relation> relationsList;

    @FXML private TextField addHumanTextField;

    private Community facebook;
    private final String db_filepath = "/Users/shook/Downloads/social_db.txt";

    private void updateData() {
        ObservableList<Human> people = FXCollections.observableArrayList(facebook.people);
        peopleList.setItems(people);

        ObservableList<Relation> relations = FXCollections.observableArrayList(facebook.relations);
        relationsList.setItems(relations);
    }

    public void initialize() {
        peopleList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Path db = Paths.get(db_filepath);

        if (Files.exists(db)) try {
            String[] read = Files.readAllLines(db).toArray(new String[0]);
            facebook = new Community(read);
        } catch (IOException e) {
            facebook = new Community();
        } else facebook = new Community();

        updateData();
    }

    public void setAddHumanHandler() {
        if (!addHumanTextField.getText().equals("")) {
            facebook.addHuman(new Human(addHumanTextField.getText()));
            addHumanTextField.setText("");
        }
        updateData();
    }

    public void saveHandler() {
        try (PrintWriter writer = new PrintWriter(db_filepath)) {
            writer.print(facebook.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeHumanHandler() {
        if (!peopleList.getSelectionModel().isEmpty()) {
            for (Human selected: peopleList.getSelectionModel().getSelectedItems()) {
                facebook.removeHuman(selected);
            }
        }
        updateData();
    }

    public void removeRelationHandler() {
        if (!relationsList.getSelectionModel().isEmpty()) {
            for (Relation selected: relationsList.getSelectionModel().getSelectedItems()) {
                facebook.removeRelation(selected);
            }
        }
        updateData();
    }

    public void showFriendsHandler() {
        if (!peopleList.getSelectionModel().isEmpty()) {
            Human selected = peopleList.getSelectionModel().getSelectedItem();

            Dialog<Void> dialog = new Dialog<>();

            dialog.setHeaderText("Friends of " + selected.getName());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);

            ListView<Human> relationsList = new ListView<>();
            relationsList.setItems(FXCollections.observableArrayList(facebook.findRelated(selected)));

            VBox pane = new VBox();
            pane.getChildren().add(relationsList);

            dialog.getDialogPane().setContent(pane);
            dialog.showAndWait();
        }
    }

    public void showFriendliestHandler() {
        Human mostFriendly = facebook.findMostFriendly();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Most friendly");
        alert.setHeaderText("This is the friendliest man of the world");
        alert.setContentText(mostFriendly.getName());

        alert.showAndWait();
    }

    public void showRecommendationsHandler() {
        if (!peopleList.getSelectionModel().isEmpty()) {
            Human selected = peopleList.getSelectionModel().getSelectedItem();
            ObservableList<Human> recommendations = FXCollections.observableArrayList(facebook.findRecommendations(selected));

            Dialog<Void> dialog = new Dialog<>();

            dialog.setHeaderText("Recommendations for " + selected.getName());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);

            ListView<Human> recommendationsList = new ListView<>();
            recommendationsList.setItems(recommendations);

            VBox pane = new VBox();
            pane.getChildren().add(recommendationsList);

            dialog.getDialogPane().setContent(pane);
            dialog.showAndWait();
        }
    }

    public void relateHandler() {
        if (!peopleList.getSelectionModel().isEmpty() && peopleList.getSelectionModel().getSelectedItems().size() == 2) {
            facebook.addRelation(peopleList.getSelectionModel().getSelectedItems().get(0), peopleList.getSelectionModel().getSelectedItems().get(1));
        }

        updateData();
    }
}
