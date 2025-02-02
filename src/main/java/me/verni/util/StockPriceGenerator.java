package me.verni.util;

import me.verni.stock.Stock;
import me.verni.stock.StockService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StockPriceGenerator {

    private final Map<String, Double> volatility = new HashMap<>();
    private final Random random = new Random();
    private double marketTrend = 0.01;
    private final StockService stockService;

    public StockPriceGenerator(StockService stockService) {
        this.stockService = stockService;
    }

    public void updateStockPricesAndSave() {
        Map<String, Double> stockPrices = new HashMap<>();
        try {
            List<Stock> stocks = stockService.getAllStocks();
            if(stocks.isEmpty()){
                return;
            }
            for (Stock stock : stocks) {
                stockPrices.put(stock.getSymbol(), stock.getPrice());
                if (!volatility.containsKey(stock.getSymbol())) {
                    volatility.put(stock.getSymbol(), 0.01 + random.nextDouble() * 0.05);
                }
            }
            updatePrices(stockPrices, stocks);
            for (Stock stock : stocks) {
                if (stockPrices.containsKey(stock.getSymbol())) {
                    double currentPrice = stockPrices.get(stock.getSymbol());
                    double previousPrice = stock.getPreviousPrice();
                    double change = calculateChange(currentPrice, previousPrice);
                    stock.setChange(change);
                    stock.setPreviousPrice(currentPrice);
                    stock.setPrice(currentPrice);
                    stockService.updateStock(stock.getId(), stock);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double calculateChange(double currentPrice, double previousPrice) {
        if (previousPrice == 0) {
            return 0;
        }
        double change = ((currentPrice - previousPrice) / previousPrice) * 100;
        BigDecimal bd = new BigDecimal(change).setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void updatePrices(Map<String, Double> stockPrices, List<Stock> stocks) {
        marketTrend += -0.001 + 0.002 * random.nextDouble();
        for (String symbol : stockPrices.keySet()) {
            double currentPrice = stockPrices.get(symbol);
            double stockVolatility = volatility.get(symbol);
            double percentageChange = marketTrend + (-stockVolatility + 2 * stockVolatility * random.nextDouble());
            double newPrice = currentPrice * (1 + percentageChange);
            BigDecimal bd = new BigDecimal(Math.max(1.0, newPrice)).setScale(2, RoundingMode.HALF_UP);
            stockPrices.put(symbol, bd.doubleValue());
        }
        for (Stock stock : stocks) {
            if (stock.getPreviousPrice() == 0) {
                stock.setPreviousPrice(stock.getPrice());
            }
        }
    }

    public Map<String, Double> getCurrentStockPrices() {
        Map<String, Double> stockPrices = new HashMap<>();
        List<Stock> stocks = stockService.getAllStocks();
        for (Stock stock : stocks) {
            stockPrices.put(stock.getSymbol(), stock.getPrice());
        }
        return stockPrices;
    }
}