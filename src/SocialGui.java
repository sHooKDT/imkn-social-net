import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SocialGui extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("SocialGui.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("VTerakte");
        primaryStage.setScene(scene);
        primaryStage.show();
        init();
    }

    public static void main(String[] args) {
        launch(SocialGui.class, args);
    }
}
