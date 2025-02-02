package me.verni.user;

import me.verni.util.NumberFormatter;

public class UserStockData {
    private String stockName;
    private String stockSymbol;
    private double purchasePrice;
    private double currentPrice;
    private double changePercent;
    private Long stockId;
    private int quantity;
    private String formattedPurchasePrice;
    private String formattedCurrentPrice;
    private String formattedQuantity;


    public UserStockData(String stockName, String stockSymbol, double purchasePrice, double currentPrice, double changePercent, Long stockId, int quantity) {
        this.stockName = stockName;
        this.stockSymbol = stockSymbol;
        this.purchasePrice = purchasePrice;
        this.currentPrice = currentPrice;
        this.changePercent = changePercent;
        this.stockId = stockId;
        this.quantity = quantity;
        this.formattedPurchasePrice = NumberFormatter.formatNumber(purchasePrice);
        this.formattedCurrentPrice = NumberFormatter.formatNumber(currentPrice);
        this.formattedQuantity = NumberFormatter.formatNumber(quantity);
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
        this.formattedPurchasePrice = NumberFormatter.formatNumber(purchasePrice);
    }

    public String getFormattedPurchasePrice() {
        return formattedPurchasePrice;
    }

    public void setFormattedPurchasePrice(String formattedPurchasePrice) {
        this.formattedPurchasePrice = formattedPurchasePrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
        this.formattedCurrentPrice = NumberFormatter.formatNumber(currentPrice);
    }

    public String getFormattedCurrentPrice() {
        return formattedCurrentPrice;
    }

    public void setFormattedCurrentPrice(String formattedCurrentPrice) {
        this.formattedCurrentPrice = formattedCurrentPrice;
    }


    public double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.formattedQuantity = NumberFormatter.formatNumber(quantity);
    }

    public String getFormattedQuantity() {
        return formattedQuantity;
    }

    public void setFormattedQuantity(String formattedQuantity) {
        this.formattedQuantity = formattedQuantity;
    }
}