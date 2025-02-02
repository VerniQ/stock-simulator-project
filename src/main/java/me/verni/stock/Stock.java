package me.verni.stock;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import me.verni.transaction.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Symbol is required")
    private String symbol;

    @Positive(message = "Price must be positive")
    private double price;
    @Positive(message = "Price must be positive")
    private double previousPrice;
    private double change;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    public Stock() {
    }

    public Stock(String name, String symbol, double price, double change) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.change = change;
    }
    public Stock(String name, String symbol, double price,double previousPrice ,double change) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.previousPrice = previousPrice;
        this.change = change;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        BigDecimal bd = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
        this.price = bd.doubleValue();
    }
    public double getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(double previousPrice) {
        this.previousPrice = previousPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public String toString() {
        return name + " - " + symbol + " - " + price;
    }
}