package me.verni.manager;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.verni.util.Constants;

import java.io.IOException;

public class SceneManager {

    private Scene scene;
    private Stage stage;
    private Parent root;

    public void switchToScene(ActionEvent event, String stageFile) throws IOException {
        root = FXMLLoader.load(getClass().getResource(stageFile));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, Constants.DEFAULT_WINDOW_WIDTH, Constants.DEFAULT_WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

    }
}
