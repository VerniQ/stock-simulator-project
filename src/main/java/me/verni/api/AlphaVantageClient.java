package me.verni.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlphaVantageClient {

    //Opcja wyłączona ze względu na ograniczenia darmowego planu
    private static final String API_KEY = "FVKXLTDZDSKREYPL";
    private static final String BASE_URL = "https://www.alphavantage.co/query?function=BATCH_STOCK_QUOTES&symbols=";

    private final OkHttpClient client = new OkHttpClient();


    public Map<String, Double> getStockPrices(List<String> symbols) throws IOException {
        String symbolsList = String.join(",", symbols);

        String url = BASE_URL + symbolsList + "&apikey=" + API_KEY;

        String response = sendRequest(url);

        return parseStockPrices(response);
    }

    private String sendRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }

    private Map<String, Double> parseStockPrices(String response) {
        Map<String, Double> stockPrices = new HashMap<>();

        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        if (jsonResponse.has("Stock Quotes")) {
            JsonArray stockQuotes = jsonResponse.getAsJsonArray("Stock Quotes");

            for (int i = 0; i < stockQuotes.size(); i++) {
                JsonObject stock = stockQuotes.get(i).getAsJsonObject();
                String symbol = stock.get("1. symbol").getAsString();
                double price = stock.get("2. price").getAsDouble();
                stockPrices.put(symbol, price);
            }
        } else {
            System.out.println("Brak danych w odpowiedzi.");
        }

        return stockPrices;
    }
}
