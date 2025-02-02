package me.verni.stock;

import java.util.List;
import java.util.Optional;

public interface StockRepository {

    List<Stock> getAllStocks();

    Optional<Stock> getStockById(Long id);

    Stock createStock(Stock stock);

    Stock updateStock(Long id, Stock updatedStock);

    void deleteStock(Long id);
}
