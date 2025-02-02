package me.verni.view.controler.transcation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.verni.manager.SceneManager;
import me.verni.transaction.Transaction;
import me.verni.transaction.TransactionService;
import me.verni.util.AlertManager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TransactionManagementController implements Initializable {

    private final SceneManager sceneManager = new SceneManager();
    private final TransactionService transactionService = new TransactionService();
    private AlertManager alertManager = new AlertManager();


    @FXML
    private TableView<Transaction> transactionsTableView;
    @FXML
    private TableColumn<Transaction, String> userNameColumn;
    @FXML
    private TableColumn<Transaction, String> stockNameColumn;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUser().getName()));
        stockNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStock().getName()));

        loadTransactions();
    }

    private void loadTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        ObservableList<Transaction> observableTransactions = FXCollections.observableArrayList(transactions);
        transactionsTableView.setItems(observableTransactions);
        transactionsTableView.refresh();
    }
    @FXML
    private void handleViewDetails() {
        Transaction selectedTransaction = transactionsTableView.getSelectionModel().getSelectedItem();
        if(selectedTransaction == null){
            alertManager.showAlert("Błąd", "Wybierz transakcję, aby zobaczyć szczegóły.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transaction/TransactionDetailsView.fxml"));
            Parent root = loader.load();
            TransactionDetailsController controller = loader.getController();
            controller.setTransaction(selectedTransaction);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Szczegóły transakcji");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e){
            e.printStackTrace();
            alertManager.showAlert("Błąd", "Wystąpił błąd podczas wyświetlania szczegółów.");
        }
    }


    @FXML
    private void handleDelete() {
        Transaction selectedTransaction = transactionsTableView.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            alertManager.showAlert("Nie wybrano transakcji", "Wybierz transakcję do usunięcia");
            return;
        }
        Optional<ButtonType> result = alertManager.showConfirmationAlert("Usuwanie transakcji", "Czy na pewno chcesz usunąć transakcję ?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                transactionService.deleteTransaction(selectedTransaction.getId());
                loadTransactions();
                alertManager.showAlert("Usunięto", "Transakcja została usunięta");
            } catch (IllegalArgumentException e) {
                alertManager.showAlert("Błąd", "Nie znaleziono transakcji do usunięcia");
            } catch (Exception e) {
                alertManager.showAlert("Błąd", "Wystąpił błąd podczas usuwania: " + e.getMessage());
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