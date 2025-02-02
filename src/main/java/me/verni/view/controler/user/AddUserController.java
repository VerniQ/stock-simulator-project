package me.verni.view.controler.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.verni.user.UserService;
import me.verni.util.AlertManager;

public class AddUserController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField balanceField;

    private UserService userService = new UserService();
    private AlertManager alertManager = new AlertManager();

    @FXML
    private void handleAddUser(ActionEvent event) {
        String name = nameField.getText();
        String balanceText = balanceField.getText();

        if (name == null || name.isEmpty() || balanceText == null || balanceText.isEmpty()) {
            alertManager.showAlert("Błąd", "Wypełnij wszystkie pola");
            return;
        }

        try {
            double balance = Double.parseDouble(balanceText);
            userService.createUser(name, balance);
            alertManager.showAlert("Dodano", "Użytkownik " + name + " został dodany");
            closeWindow(event);
        } catch (NumberFormatException e) {
            alertManager.showAlert("Błąd", "Niepoprawny format salda");
        } catch (Exception e) {
            alertManager.showAlert("Błąd", "Wystąpił błąd podczas dodawania użytkownika: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow(event);
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}