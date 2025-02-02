package me.verni.view.controler.simulator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import me.verni.manager.SceneManager;
import me.verni.user.User;
import me.verni.util.NumberFormatter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserDetailsController implements Initializable {
    private final SceneManager sceneManager = new SceneManager();
    @FXML
    private Label userNameLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label idLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = UserSession.getUser();
        if (user != null){
            userNameLabel.setText(user.getName());
            balanceLabel.setText(NumberFormatter.formatNumber(user.getBalance()));
            idLabel.setText(String.valueOf(user.getId()));
        }
    }
    @FXML
    public void handleBack(ActionEvent event) throws IOException {
        sceneManager.switchToScene(event, "/fxml/simulator/UserProfileView.fxml");
    }
}