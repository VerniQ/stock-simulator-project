package me.verni.view.controler.simulator;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import me.verni.manager.SceneManager;
import me.verni.stock.Stock;
import me.verni.stock.StockService;
import me.verni.transaction.TransactionService;
import me.verni.user.User;
import me.verni.util.AlertManager;
import me.verni.util.Constants;
import me.verni.util.NumberFormatter;
import me.verni.util.StockPriceGenerator;
import me.verni.view.controler.simulator.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserMarketController implements Initializable {
    private final SceneManager sceneManager = new SceneManager();
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
    private final StockService stockService = new StockService();
    private final TransactionService transactionService = new TransactionService();
    private ScheduledExecutorService scheduler;
    private final StockPriceGenerator priceGenerator = new StockPriceGenerator(stockService);
    private final ObservableList<Stock> observableStocks = FXCollections.observableArrayList();
    AlertManager alertManager = new AlertManager();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Ustawienie powiązań kolumn z właściwościami obiektu Stock
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        priceColumn.setCellValueFactory(cellData -> {
            String formattedPrice = NumberFormatter.formatNumber(cellData.getValue().getPrice());
            return new javafx.beans.property.SimpleStringProperty(formattedPrice);
        });

        changePercentColumn.setCellValueFactory(new PropertyValueFactory<>("change"));

        // Ustawienie CellFactory dla kolumny "Zmiana w %", aby kolorować komórki
        changePercentColumn.setCellFactory(column -> new TableCell<Stock, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%.2f", item) + "%"); // Dodajemy % po wartości
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
        stocksTableView.setItems(observableStocks);
        stocksTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("Wybrano akcję z tabeli: " + newSelection.getName() + " " + newSelection.getSymbol() + " " + newSelection.getId());
            }
        });
        startUpdating();
    }
    private void startUpdating(){
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(
                () -> {
                    try{
                        priceGenerator.updateStockPricesAndSave();
                        Platform.runLater(() -> {
                            int selectedIndex = stocksTableView.getSelectionModel().getSelectedIndex(); // zapamiętanie indeksu wybranej akcji

                            List<Stock> stocks = getStocks();
                            observableStocks.clear(); // czyszczenie listy
                            observableStocks.addAll(stocks);
                            if (selectedIndex >= 0 && selectedIndex < observableStocks.size()) { // jeżeli jakiś element był wybrany to zaznacz go
                                stocksTableView.getSelectionModel().select(selectedIndex);
                            }
                            stocksTableView.refresh();
                        });
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                },
                0,
                Constants.UPDATE_TIME_IN_SECONDS, // Opóźnienie między wykonaniami (10 sekund)
                TimeUnit.SECONDS
        );
    }
    private void stopUpdating(){
        if(scheduler != null){
            scheduler.shutdown();
        }
    }
    private void loadStocks() {
        List<Stock> stocks = getStocks();
        observableStocks.addAll(stocks);
    }

    private List<Stock> getStocks() {
        return stockService.getAllStocks();
    }

    @FXML
    public void handleBuy(ActionEvent event) {
        Stock selectedStock = stocksTableView.getSelectionModel().getSelectedItem();
        if(selectedStock == null){
            alertManager.showAlert("Nie wybrano akcji", "Wybierz akcję do kupienia");
            return;
        }
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Kup akcję");
        dialog.setHeaderText("Podaj ilość akcji do kupienia");
        dialog.setContentText("Ilość:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(quantityString -> {
            try{
                int quantity = Integer.parseInt(quantityString);
                if(quantity <= 0){
                    alertManager.showAlert("Niepoprawna ilość", "Ilość musi być większa od 0");
                    return;
                }
                Stock updatedSelectedStock = stocksTableView.getSelectionModel().getSelectedItem();
                if (updatedSelectedStock == null){
                    alertManager.showAlert("Nie wybrano akcji", "Wybierz akcję do kupienia");
                    return;
                }
                User user = UserSession.getUser();
                transactionService.createTransaction(user.getId(),  updatedSelectedStock.getId(),quantity,updatedSelectedStock.getPrice());
                alertManager.showAlert("Kupiono", "Kupiłeś " + quantity + " akcji " + updatedSelectedStock.getName());
            } catch(NumberFormatException e){
                alertManager.showAlert("Niepoprawny format ilości", "Podaj liczbę całkowitą");
            }
            catch (IllegalArgumentException e){
                alertManager.showAlert("Błąd", e.getMessage());
            }
            catch (Exception e){
                alertManager.showAlert("Błąd", "Wystąpił błąd podczas zakupu akcji");
                e.printStackTrace();
            }

        });
    }
    @FXML
    public void handleBack(ActionEvent event) throws IOException {
        stopUpdating();
        sceneManager.switchToScene(event, "/fxml/simulator/UserProfileView.fxml");
    }
}