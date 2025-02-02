package me.verni.stock;

import me.verni.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;


import java.util.List;
import java.util.Optional;

public class StockService implements StockRepository {


    public List<Stock> getAllStocks() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Stock", Stock.class).list();
        }
    }

    public Optional<Stock> getStockById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Stock.class, id));
        }
    }

    public Stock createStock(Stock stock) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(stock);
            transaction.commit();
            return stock;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Stock updateStock(Long id, Stock updatedStock) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Stock stock = session.get(Stock.class, id);
            if (stock != null) {
                stock.setSymbol(updatedStock.getSymbol());
                stock.setPrice(updatedStock.getPrice());
                stock.setChange(updatedStock.getChange());
                stock.setPreviousPrice(updatedStock.getPreviousPrice());
                session.update(stock);
                transaction.commit();
                return stock;
            } else {
                transaction.rollback();
                throw new IllegalArgumentException("Stock not found with ID: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }


    public void deleteStock(Long id) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Stock stock = session.get(Stock.class, id);
            if (stock != null) {
                session.delete(stock);
                transaction.commit();
            } else {
                throw new IllegalArgumentException("Stock not found with ID: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if(session != null) {
                session.close();
            }
        }
    }
}