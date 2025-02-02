package me.verni.view.controler.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.verni.manager.SceneManager;
import me.verni.user.User;
import me.verni.user.UserService;
import me.verni.util.AlertManager;
import me.verni.util.NumberFormatter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserManagementController implements Initializable {

    private SceneManager sceneManager = new SceneManager();
    private UserService userService = new UserService();
    private AlertManager alertManager = new AlertManager();

    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> balanceColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        balanceColumn.setCellValueFactory(cellData -> {
            String formattedBalance = NumberFormatter.formatNumber(cellData.getValue().getBalance());
            return new javafx.beans.property.SimpleStringProperty(formattedBalance);
        });

        createContextMenu();
        loadUsers();
    }

    private void createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem updateBalanceItem = new MenuItem("Aktualizuj saldo");
        updateBalanceItem.setOnAction(event -> handleContextMenuUpdate());
        contextMenu.getItems().add(updateBalanceItem);
        userTableView.setContextMenu(contextMenu);
    }

    private void loadUsers() {
        List<User> users = userService.getAllUsers();
        ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
        userTableView.setItems(observableUsers);
        userTableView.refresh();
    }

    @FXML
    private void handleAddUser(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/AddUserView.fxml"));
            Parent root = loader.load();
            AddUserController controller = loader.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Dodaj Użytkownika");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnHidden(e -> loadUsers());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleContextMenuUpdate() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            alertManager.showAlert("Nie wybrano użytkownika", "Wybierz użytkownika do zaktualizowania");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.valueOf(selectedUser.getBalance()));
        dialog.setTitle("Aktualizacja Salda");
        dialog.setHeaderText("Podaj nowe saldo dla użytkownika " + selectedUser.getName());
        dialog.setContentText("Saldo:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(balanceText -> {
            try {
                double balance = Double.parseDouble(balanceText);
                User updatedUser = userService.updateBalance(selectedUser.getId(), balance);
                loadUsers();
                alertManager.showAlert("Zaktualizowano", "Saldo użytkownika " + updatedUser.getName() + " zostało zaktualizowane");
            } catch (NumberFormatException e) {
                alertManager.showAlert("Błąd", "Niepoprawny format salda, wprowadź liczbę.");
            } catch (IllegalArgumentException e) {
                alertManager.showAlert("Błąd", "Nie znaleziono użytkownika do zaktualizowania");
            } catch (Exception e) {
                alertManager.showAlert("Błąd", "Wystąpił błąd podczas aktualizowania salda " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            alertManager.showAlert("Nie wybrano użytkownika", "Wybierz użytkownika do usunięcia");
            return;
        }

        Optional<ButtonType> result = alertManager.showConfirmationAlert("Usuwanie użytkownika", "Czy na pewno chcesz usunąć użytkownika " + selectedUser.getName() + " ?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                userService.deleteUser(selectedUser.getId());
                loadUsers();
                alertManager.showAlert("Usunięto", "Użytkownik " + selectedUser.getName() + " został usunięty");
            } catch (IllegalArgumentException e) {
                alertManager.showAlert("Błąd", "Nie znaleziono użytkownika do usunięcia");
            } catch (Exception e) {
                alertManager.showAlert("Błąd", "Wystąpił błąd podczas usuwania " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            sceneManager.switchToScene(event, "/fxml/MainView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}