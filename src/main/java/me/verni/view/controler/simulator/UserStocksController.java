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
import me.verni.transaction.Transaction;
import me.verni.transaction.TransactionService;
import me.verni.user.User;
import me.verni.user.UserStockData;
import me.verni.util.AlertManager;
import me.verni.util.Constants;
import me.verni.util.StockPriceGenerator;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserStocksController implements Initializable {

    private final SceneManager sceneManager = new SceneManager();
    @FXML
    private TableView<UserStockData> stocksTableView;
    @FXML
    private TableColumn<UserStockData, String> nameColumn;
    @FXML
    private TableColumn<UserStockData, String> symbolColumn;
    @FXML
    private TableColumn<UserStockData, String> purchasePriceColumn;
    @FXML
    private TableColumn<UserStockData, String> currentPriceColumn;
    @FXML
    private TableColumn<UserStockData, Double> changePercentColumn;
    @FXML
    private TableColumn<UserStockData, String> quantityColumn;


    private final TransactionService transactionService = new TransactionService();
    private final StockPriceGenerator priceGenerator;
    private final ScheduledExecutorService scheduler;
    private final ObservableList<UserStockData> observableStocks = FXCollections.observableArrayList();
    private User user;
    private AlertManager alertManager = new AlertManager();


    public UserStocksController() {
        this.priceGenerator = new StockPriceGenerator(new me.verni.stock.StockService());
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Ustawienie powiązań kolumn z właściwościami obiektu UserStockData
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("stockName"));
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("stockSymbol"));
        purchasePriceColumn.setCellValueFactory(new PropertyValueFactory<>("formattedPurchasePrice"));
        currentPriceColumn.setCellValueFactory(new PropertyValueFactory<>("formattedCurrentPrice"));
        changePercentColumn.setCellValueFactory(new PropertyValueFactory<>("changePercent"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("formattedQuantity"));


        // Ustawienie CellFactory dla kolumny "Zmiana w %", aby kolorować komórki
        changePercentColumn.setCellFactory(column -> new TableCell<UserStockData, Double>() {
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
        user = UserSession.getUser();
        loadUserStocks();
        stocksTableView.setItems(observableStocks);
        startUpdating();
    }

    private void startUpdating() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                priceGenerator.updateStockPricesAndSave();
                Platform.runLater(() -> {
                    loadUserStocks();
                    stocksTableView.refresh();
                    System.out.println("Zaktualizowano ceny akcji - " + priceGenerator.getCurrentStockPrices());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, Constants.UPDATE_TIME_IN_SECONDS, TimeUnit.SECONDS);
    }

    private void stopUpdating() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    private void loadUserStocks() {
        observableStocks.clear();
        List<Transaction> userTransactions = transactionService.getTransactionsByUser(user.getId());
        Map<String, Double> currentPrices = priceGenerator.getCurrentStockPrices();

        if (userTransactions.isEmpty()) {
            return;
        }

        Map<Long, UserStockData> userStocks = new HashMap<>();
        for (Transaction transaction : userTransactions) {
            Stock stock = transaction.getStock();
            double currentPrice = currentPrices.getOrDefault(stock.getSymbol(), stock.getPrice());

            if (userStocks.containsKey(stock.getId())) {
                UserStockData userStockData = userStocks.get(stock.getId());
                userStockData.setQuantity(userStockData.getQuantity() + transaction.getQuantity());
            } else {
                UserStockData userStockData = new UserStockData(
                        stock.getName(),
                        stock.getSymbol(),
                        transaction.getPrice(),
                        currentPrice,
                        0,
                        stock.getId(),
                        transaction.getQuantity()
                );
                userStocks.put(stock.getId(), userStockData);
            }
        }
        //filtrujemy liste
        userStocks.values().stream()
                .filter(userStockData -> userStockData.getQuantity() > 0) // zostawiamy tylko akcje, których mamy więcej niż 0
                .forEach(userStockData -> {
                    double change = calculateChange(userStockData.getCurrentPrice(), userStockData.getPurchasePrice());
                    userStockData.setChangePercent(change);
                    observableStocks.add(userStockData);
                });
    }

    private double calculateChange(double currentPrice, double purchasePrice) {
        if (purchasePrice == 0) return 0;
        double change = ((currentPrice - purchasePrice) / purchasePrice) * 100;
        return Math.round(change * 100.0) / 100.0; // Zaokrąglenie do 2 miejsc po przecinku
    }
    @FXML
    public void handleSell(ActionEvent event) {
        UserStockData selectedStockData = stocksTableView.getSelectionModel().getSelectedItem();
        if (selectedStockData == null) {
            alertManager.showAlert("Nie wybrano akcji", "Wybierz akcję do sprzedania");
            return;
        }
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Sprzedaj akcję");
        dialog.setHeaderText("Podaj ilość akcji do sprzedania");
        dialog.setContentText("Ilość:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(quantityString -> {
            try {
                int quantity = Integer.parseInt(quantityString);
                if (quantity <= 0) {
                    alertManager.showAlert("Niepoprawna ilość", "Ilość musi być większa od 0");
                    return;
                }
                if (quantity > selectedStockData.getQuantity()) {
                    alertManager.showAlert("Niepoprawna ilość", "Posiadasz " + selectedStockData.getQuantity() + " akcji, nie możesz sprzedać " + quantity);
                    return;
                }
                Stock stock = new me.verni.stock.StockService().getStockById(selectedStockData.getStockId()).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono akcji do sprzedaży"));
                transactionService.createTransaction(user.getId(), stock.getId(), -quantity, selectedStockData.getCurrentPrice()); //przekazujemy ujemna ilosc do transakcji
                alertManager.showAlert("Sprzedano", "Sprzedałeś " + quantity + " akcji " + selectedStockData.getStockName());
                loadUserStocks();
                stocksTableView.refresh();

            } catch (NumberFormatException e) {
                alertManager.showAlert("Niepoprawny format ilości", "Podaj liczbę całkowitą");
            } catch (IllegalArgumentException e) {
                alertManager.showAlert("Błąd", e.getMessage());
            } catch (Exception e) {
                alertManager.showAlert("Błąd", "Wystąpił błąd podczas sprzedaży akcji");
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