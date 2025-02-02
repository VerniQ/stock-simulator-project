package me.verni.view.controler.stock;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.verni.stock.Stock;
import me.verni.stock.StockService;
import me.verni.util.AlertManager;

public class AddStockController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField symbolField;
    @FXML
    private TextField priceField;

    private StockService stockService = new StockService();
    private AlertManager alertManager = new AlertManager();


    @FXML
    private void handleAddStock(ActionEvent event) {
        try {
            String name = nameField.getText();
            String symbol = symbolField.getText();
            double price = Double.parseDouble(priceField.getText());

            if(name.isEmpty() || symbol.isEmpty() || priceField.getText().isEmpty()){
                alertManager.showAlert("Błąd", "Wypełnij wszystkie pola");
                return;
            }
            //Domyślna zmiana procentowa ustawiona na 0
            Stock newStock = new Stock(name,symbol,price,0.0);
            stockService.createStock(newStock);
            closeWindow(event);
        } catch (NumberFormatException e) {
            alertManager.showAlert("Błąd", "Niepoprawny format ceny");
        }
        catch (Exception e){
            alertManager.showAlert("Błąd", e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow(event);
    }

    private void closeWindow(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}