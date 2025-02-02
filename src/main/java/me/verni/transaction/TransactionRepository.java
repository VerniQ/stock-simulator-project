package me.verni.transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {


    List<Transaction> getAllTransactions();


    Optional<Transaction> getTransactionById(Long id);


    Transaction createTransaction(Long userId, Long stockId, int quantity, double price);


    List<Transaction> getTransactionsByUser(Long userId);


    void deleteTransaction(Long id);
}
