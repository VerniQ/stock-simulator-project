package me.verni.view.controler;

import javafx.event.ActionEvent;
import me.verni.manager.SceneManager;

import java.io.IOException;

public class MainController {
    private SceneManager sceneManager = new SceneManager();

    public void handleUsers(ActionEvent event) {
        try {
            sceneManager.switchToScene(event, "/fxml/user/UsersView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleStocks(ActionEvent event) {
        try {
            sceneManager.switchToScene(event, "/fxml/stock/StockView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handleTransactions(ActionEvent event) {
        try {
            sceneManager.switchToScene(event, "/fxml/transaction/TransactionsView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleSimulator(ActionEvent event) {
            try {
                sceneManager.switchToScene(event, "/fxml/simulator/UserSelectView.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void handleClose(ActionEvent event) {
        System.exit(0);
    }

}