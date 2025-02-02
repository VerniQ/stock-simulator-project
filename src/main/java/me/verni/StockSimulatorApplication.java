package me.verni;

import javafx.application.Application;
import me.verni.util.DefaultStocksInitializer;
import me.verni.util.HibernateUtil;
import me.verni.window.Start;


public class StockSimulatorApplication {

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory();
        Application.launch(Start.class, args);

        DefaultStocksInitializer defaultStocksInitializer = new DefaultStocksInitializer();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            HibernateUtil.getSessionFactory().close();
        }));

    }
}

