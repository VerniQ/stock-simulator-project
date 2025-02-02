package me.verni.view.controler.transcation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import me.verni.transaction.Transaction;
import me.verni.util.NumberFormatter;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TransactionDetailsController implements Initializable {
    @FXML
    private Label idLabel;
    @FXML
    private Label userLabel;
    @FXML
    private Label stockLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label dateLabel;

    private Transaction transaction;


    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        updateLabels();
    }

    private void updateLabels() {
        if(transaction != null) {
            idLabel.setText(String.valueOf(transaction.getId()));
            userLabel.setText(transaction.getUser().getName());
            stockLabel.setText(transaction.getStock().getName());
            quantityLabel.setText(NumberFormatter.formatNumber(transaction.getQuantity()));
            priceLabel.setText(NumberFormatter.formatNumber(transaction.getPrice()));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = transaction.getDate().format(formatter);
            dateLabel.setText(formattedDate);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}