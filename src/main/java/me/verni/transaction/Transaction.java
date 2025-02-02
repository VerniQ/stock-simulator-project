package me.verni.transaction;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import me.verni.stock.Stock;
import me.verni.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Positive(message = "Quantity must be positive")
    private int quantity;

    @Positive(message = "Price must be positive")
    private double price;

    @NotNull(message = "Date is required")
    private LocalDateTime date;


    public Transaction() {
    }

    public Transaction(Long id, User user, Stock stock, int quantity, double price, LocalDateTime date) {
        this.id = id;
        this.user = user;
        this.stock = stock;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
    }

    public Transaction(User user, Stock stock, int quantity, double price) {
        this.user = user;
        this.stock = stock;
        this.quantity = quantity;
        this.price = price;
        this.date = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}