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
import java.util.List;

public class SocialGuiController {

    @FXML private ListView<Human> peopleList;
    @FXML private ListView<Relation> relationsList;

    @FXML private TextField addHumanTextField;

    private Community facebook;
    private final String db_filepath = "/home/shook/Downloads/social_db.txt";

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

    private <T> void showListDialog(List<T> list, String header) {
        ObservableList<T> observableList = FXCollections.observableArrayList(list);

        Dialog<Void> dialog = new Dialog<>();

        dialog.setHeaderText(header);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        ListView<T> recommendationsList = new ListView<>();
        recommendationsList.setItems(observableList);

        VBox pane = new VBox();
        pane.getChildren().add(recommendationsList);

        dialog.getDialogPane().setContent(pane);
        dialog.showAndWait();
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
            showListDialog(facebook.findRelated(selected), "Friends of " + selected.getName());
        }
    }

    public void showFriendliestHandler() {
        List<Human> mostFriendly = facebook.findMostFriendly();

        if (mostFriendly.size() == 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Most friendly");
            alert.setHeaderText("This is the friendliest man of the world");
            alert.setContentText(mostFriendly.get(0).getName());
            alert.showAndWait();
        } else {
            showListDialog(mostFriendly, "Friendliest people");
        }
    }

    public void showRecommendationsHandler() {
        if (!peopleList.getSelectionModel().isEmpty()) {
            Human selected = peopleList.getSelectionModel().getSelectedItem();
            showListDialog(facebook.findRecommendations(selected), "Recommendations for " + selected.getName());
        }
    }

    public void relateHandler() {
        if (!peopleList.getSelectionModel().isEmpty() && peopleList.getSelectionModel().getSelectedItems().size() == 2) {
            facebook.addRelation(peopleList.getSelectionModel().getSelectedItems().get(0), peopleList.getSelectionModel().getSelectedItems().get(1));
        }

        updateData();
    }
}
