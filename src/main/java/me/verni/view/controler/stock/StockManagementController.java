package me.verni.view.controler.stock;

import javafx.application.Platform;
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
import javafx.stage.WindowEvent;
import me.verni.manager.SceneManager;
import me.verni.stock.Stock;
import me.verni.stock.StockService;
import me.verni.util.AlertManager;
import me.verni.util.Constants;
import me.verni.util.NumberFormatter;
import me.verni.util.StockPriceGenerator;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StockManagementController implements Initializable {
    private SceneManager sceneManager = new SceneManager();
    @FXML
    private TableView<Stock> stocksTableView;
    @FXML
    private TableColumn<Stock, String> nameColumn;
    @FXML
    private TableColumn<Stock, String> symbolColumn;
    @FXML
    private TableColumn<Stock, String> priceColumn;
    @FXML
    private TableColumn<Stock, Double> changePercentColumn;

    private StockService stockService = new StockService();

    private StockPriceGenerator priceGenerator = new StockPriceGenerator(stockService);
    private ScheduledExecutorService scheduler;
    private AlertManager alertManager = new AlertManager();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        priceColumn.setCellValueFactory(cellData -> {
            String formattedPrice = NumberFormatter.formatNumber(cellData.getValue().getPrice());
            return new javafx.beans.property.SimpleStringProperty(formattedPrice);
        });
        changePercentColumn.setCellValueFactory(new PropertyValueFactory<>("change"));

        changePercentColumn.setCellFactory(column -> new TableCell<Stock, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%.2f", item) + "%");
                    if (item < 0) {
                        setStyle("-fx-text-fill: red;");
                    } else if (item > 0) {
                        setStyle("-fx-text-fill: green;");
                    } else {
                        setStyle("-fx-text-fill: yellow;");
                    }
                }
            }
        });
        loadStocks();

        startUpdating();
    }
    private void startUpdating(){
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(
                () -> {
                    try{
                        priceGenerator.updateStockPricesAndSave();
                        Platform.runLater(() -> {
                            List<Stock> stocks = getStocks();
                            ObservableList<Stock> observableStocks = FXCollections.observableArrayList(stocks.stream()
                                    .map(stock -> new Stock(stock.getName(), stock.getSymbol(), stock.getPrice(), stock.getPreviousPrice(),stock.getChange()))
                                    .sorted((s1, s2) -> s1.getName().compareTo(s2.getName()))
                                    .collect(Collectors.toList()));
                            stocksTableView.setItems(observableStocks);
                            stocksTableView.refresh();

                        });
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                },
                0,
                Constants.UPDATE_TIME_IN_SECONDS,
                TimeUnit.SECONDS
        );
    }
    private void handleWindowClosing(WindowEvent event){
        if(scheduler != null){
            scheduler.shutdown();
        }
    }
    private void stopUpdating(){
        if(scheduler != null){
            scheduler.shutdown();
        }
    }
    private void loadStocks() {
        List<Stock> stocks = getStocks();
        ObservableList<Stock> observableStocks = FXCollections.observableArrayList(stocks.stream()
                .map(stock -> new Stock(stock.getName(), stock.getSymbol(), stock.getPrice(),stock.getPreviousPrice(),stock.getChange()))
                .sorted((s1, s2) -> s1.getName().compareTo(s2.getName()))
                .collect(Collectors.toList()));
        stocksTableView.setItems(observableStocks);
        stocksTableView.refresh();
    }

    private List<Stock> getStocks() {
        return stockService.getAllStocks();
    }

    @FXML
    private void handleAddStock(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/stock/AddStockView.fxml"));
            Parent root = loader.load();
            AddStockController controller = loader.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Dodaj Akcję");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnHidden(e -> loadStocks());
            stage.showAndWait();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateStock(ActionEvent event) throws IOException {
        System.out.println("Update stock");
        stopUpdating();
        sceneManager.switchToScene(event, "/fxml/stock/StockView.fxml");
    }

    @FXML
    private void handleDeleteStock() {
        Stock selectedStock = stocksTableView.getSelectionModel().getSelectedItem();
        if (selectedStock == null) {
            alertManager.showAlert("Nie wybrano akcji", "Wybierz akcję do usunięcia");
            return;
        }
        Optional<ButtonType> result = showConfirmationAlert("Usuwanie akcji","Czy na pewno chcesz usunąć akcję " + selectedStock.getName() + " ?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                stockService.deleteStock(selectedStock.getId());
                Platform.runLater(() -> {
                    loadStocks();
                });

                alertManager.showAlert("Usunięto", "Akcja " + selectedStock.getName() + " została pomyślnie usunięta");
            } catch (IllegalArgumentException e){
                alertManager.showAlert("Błąd", "Nie znaleziono akcji do usunięcia " + selectedStock.getName());
            }
            catch (Exception e) {
                alertManager.showAlert("Błąd", "Wystąpił błąd podczas usuwania: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private Optional<ButtonType> showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        stopUpdating();
        try {
            sceneManager.switchToScene(event, "/fxml/MainView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}