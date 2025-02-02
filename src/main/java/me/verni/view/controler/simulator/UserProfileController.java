package me.verni.view.controler.simulator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import me.verni.manager.SceneManager;
import me.verni.user.User;
import me.verni.view.controler.simulator.UserSession;

import java.io.IOException;

public class UserProfileController {
    private SceneManager sceneManager = new SceneManager();
    private User user;

    @FXML
    public void handleMarket(ActionEvent event) throws IOException {
        user =  UserSession.getUser();
        sceneManager.switchToScene(event, "/fxml/simulator/UserMarketView.fxml");
    }

    @FXML
    public void handleStocks(ActionEvent event) throws IOException {
        user =  UserSession.getUser();
        sceneManager.switchToScene(event, "/fxml/simulator/UserStocksView.fxml");
    }
    @FXML
    public void handleAccount(ActionEvent event) throws IOException {
        user =  UserSession.getUser();
        sceneManager.switchToScene(event, "/fxml/simulator/UserDetailsView.fxml");
    }
    @FXML
    public void handleBack(ActionEvent event) throws IOException {
        sceneManager.switchToScene(event, "/fxml/MainView.fxml");
    }
}