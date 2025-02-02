package me.verni.util;

import me.verni.stock.Stock;
import me.verni.stock.StockService;

public class DefaultStocksInitializer {

    public DefaultStocksInitializer() {
        initializeDatabase();
    }

    public void initializeDatabase() {
        StockService stockService = new StockService();
        if (stockService.getAllStocks().isEmpty()) {
            stockService.createStock(new Stock("Apple Inc", "AAPL", 189.45, 189.45, 0));
            stockService.createStock(new Stock("Microsoft Corporation", "MSFT", 333.10, 333.10, 0));
            stockService.createStock(new Stock("Alphabet Inc.", "GOOGL", 139.67, 139.67, 0));
            stockService.createStock(new Stock("Amazon.com, Inc.", "AMZN", 357.25, 357.25, 0));
            stockService.createStock(new Stock("Tesla, Inc.", "TSLA", 278.12, 278.12, 0));
            stockService.createStock(new Stock("NVIDIA Corporation", "NVDA", 420.58, 420.58, 0));
            stockService.createStock(new Stock("Meta Platforms, Inc.", "META", 302.76, 302.76, 0));
            stockService.createStock(new Stock("Berkshire Hathaway Inc.", "BRK.B", 478.13, 478.13, 0));
            stockService.createStock(new Stock("UnitedHealth Group Incorporated", "UNH", 487.92, 487.92, 0));
            stockService.createStock(new Stock("Johnson & Johnson", "JNJ", 153.21, 153.21, 0));
            stockService.createStock(new Stock("Visa Inc.", "V", 226.87, 226.87, 0));
            stockService.createStock(new Stock("Procter & Gamble Company", "PG", 164.43, 164.43, 0));
            stockService.createStock(new Stock("The Home Depot, Inc.", "HD", 290.98, 290.98, 0));
            stockService.createStock(new Stock("Mastercard Incorporated", "MA", 381.47, 381.47, 0));
            stockService.createStock(new Stock("Exxon Mobil Corporation", "XOM", 135.25, 135.25, 0));
            stockService.createStock(new Stock("Eli Lilly and Company", "LLY", 479.12, 479.12, 0));
            stockService.createStock(new Stock("Pfizer Inc.", "PFE", 103.56, 103.56, 0));
            stockService.createStock(new Stock("AbbVie Inc.", "ABBV", 146.78, 146.78, 0));
            stockService.createStock(new Stock("Coca-Cola Company", "KO", 64.32, 64.32, 0));
            stockService.createStock(new Stock("PepsiCo, Inc.", "PEP", 185.65, 185.65, 0));
            stockService.createStock(new Stock("Costco Wholesale Corporation", "COST", 463.54, 463.54, 0));
            stockService.createStock(new Stock("Walmart Inc.", "WMT", 149.39, 149.39, 0));
            stockService.createStock(new Stock("The Walt Disney Company", "DIS", 92.76, 92.76, 0));
            stockService.createStock(new Stock("McDonalds Corporation", "MCD", 281.44, 281.44, 0));
            stockService.createStock(new Stock("Adobe Inc.", "ADBE", 440.05, 440.05, 0));
            stockService.createStock(new Stock("Netflix, Inc.", "NFLX", 346.90, 346.90, 0));
            stockService.createStock(new Stock("Salesforce, Inc.", "CRM", 234.78, 234.78, 0));
            stockService.createStock(new Stock("Comcast Corporation", "CMCSA", 39.56, 39.56, 0));
            stockService.createStock(new Stock("Abbott Laboratories", "ABT", 112.87, 112.87, 0));
            stockService.createStock(new Stock("Cisco Systems, Inc.", "CSCO", 58.34, 58.34, 0));
            stockService.createStock(new Stock("Accenture plc", "ACN", 364.10, 364.10, 0));
            stockService.createStock(new Stock("Thermo Fisher Scientific Inc.", "TMO", 519.05, 519.05, 0));
            stockService.createStock(new Stock("Broadcom Inc.", "AVGO", 226.40, 226.40, 0));
            stockService.createStock(new Stock("Texas Instruments Incorporated", "TXN", 185.76, 185.76, 0));
            stockService.createStock(new Stock("NextEra Energy, Inc.", "NEE", 72.01, 72.01, 0));
            stockService.createStock(new Stock("Philip Morris International Inc.", "PM", 123.89, 123.89, 0));
            stockService.createStock(new Stock("Union Pacific Corporation", "UNP", 224.76, 224.76, 0));
            stockService.createStock(new Stock("Linde plc", "LIN", 380.94, 380.94, 0));
            stockService.createStock(new Stock("Qualcomm Incorporated", "QCOM", 151.48, 151.48, 0));
            stockService.createStock(new Stock("Nike, Inc.", "NKE", 76.12, 76.12, 0));
            stockService.createStock(new Stock("Advanced Micro Devices, Inc.", "AMD", 121.45, 121.45, 0));
            stockService.createStock(new Stock("Merck & Co., Inc.", "MRK", 98.93, 98.93, 0));
            stockService.createStock(new Stock("Honeywell International Inc.", "HON", 227.45, 227.45, 0));
            stockService.createStock(new Stock("Oracle Corporation", "ORCL", 168.34, 168.34, 0));
            stockService.createStock(new Stock("The Boeing Company", "BA", 179.23, 179.23, 0));
            stockService.createStock(new Stock("International Business Machines Corporation", "IBM", 224.11, 224.11, 0));
            stockService.createStock(new Stock("Chevron Corporation", "CVX", 141.97, 141.97, 0));
            stockService.createStock(new Stock("Caterpillar Inc.", "CAT", 366.02, 366.02, 0));
            stockService.createStock(new Stock("Amgen Inc.", "AMGN", 266.73, 266.73, 0));
            stockService.createStock(new Stock("Intel Corporation", "INTC", 19.87, 19.87, 0));
        }
    }
}
