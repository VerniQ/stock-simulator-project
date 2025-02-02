package me.verni.view.controler.simulator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import me.verni.manager.SceneManager;
import me.verni.user.User;
import me.verni.user.UserService;
import me.verni.util.AlertManager;
import me.verni.util.NumberFormatter;
import me.verni.view.controler.simulator.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserSelectController implements Initializable {
    private SceneManager sceneManager = new SceneManager();
    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> userName;
    @FXML
    private TableColumn<User, String> UserBalance;

    private  User selectedUser;
    private UserService userService = new UserService();
    private AlertManager alertManager = new AlertManager();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userName.setCellValueFactory(new PropertyValueFactory<>("name"));
        UserBalance.setCellValueFactory(cellData -> {
            String formattedBalance = NumberFormatter.formatNumber(cellData.getValue().getBalance());
            return new javafx.beans.property.SimpleStringProperty(formattedBalance);
        });
        loadUsers();
    }
    private void loadUsers() {
        List<User> users = userService.getAllUsers();
        ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
        userTableView.setItems(observableUsers);
    }

    @FXML
    public void handleNext(ActionEvent event) throws IOException {
        selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            alertManager.showAlert("Nie wybrano użytkownika", "Wybierz użytkownika z listy");
            return;
        }
        UserSession.setUser(selectedUser);
        sceneManager.switchToScene(event,"/fxml/simulator/UserProfileView.fxml");
    }
    @FXML
    public void handleBack(ActionEvent event) throws IOException {
        sceneManager.switchToScene(event, "/fxml/MainView.fxml");
    }
}