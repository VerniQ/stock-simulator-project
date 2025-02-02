package me.verni.transaction;

import me.verni.stock.Stock;
import me.verni.stock.StockRepository;
import me.verni.user.User;
import me.verni.user.UserRepository;
import me.verni.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class TransactionService implements TransactionRepository {

    private final UserRepository userRepository;
    private final StockRepository stockRepository;


    public TransactionService(UserRepository userRepository, StockRepository stockRepository) {
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
    }

    public TransactionService() {
        this.userRepository = new me.verni.user.UserService();
        this.stockRepository = new me.verni.stock.StockService();
    }


    public List<me.verni.transaction.Transaction> getAllTransactions() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Transaction", me.verni.transaction.Transaction.class).list();
        }
    }

    public Optional<me.verni.transaction.Transaction> getTransactionById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(me.verni.transaction.Transaction.class, id));
        }
    }


    public me.verni.transaction.Transaction createTransaction(Long userId, Long stockId, int quantity, double price) {
        me.verni.transaction.Transaction transaction = null;
        Transaction dbTransaction = null;
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            dbTransaction = session.beginTransaction();
            // Retrieve user and stock
            User user = session.get(User.class, userId);
            if (user == null) {
                throw new IllegalArgumentException("User not found with ID: " + userId);
            }
            Stock stock = session.get(Stock.class, stockId);
            if (stock == null) {
                throw new IllegalArgumentException("Stock not found with ID: " + stockId);
            }

            // Check balance
            if (user.getBalance() < price * quantity) {
                throw new IllegalArgumentException("Insufficient balance for the transaction.");
            }

            // Update user balance and create transaction
            user.setBalance(user.getBalance() - (price * quantity));

            transaction = new me.verni.transaction.Transaction(user, stock, quantity, price);

            session.update(user);
            session.save(transaction);

            dbTransaction.commit();
        } catch (Exception e) {
            if (dbTransaction != null) dbTransaction.rollback();
            throw e;
        } finally {
            if(session != null){
                session.close();
            }
        }

        return transaction;
    }

    public void updateTransaction(me.verni.transaction.Transaction transaction) {
        Transaction dbTransaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            dbTransaction = session.beginTransaction();
            session.merge(transaction);
            dbTransaction.commit();

        } catch (Exception e) {
            if (dbTransaction != null) dbTransaction.rollback();
            throw e;
        }

    }

    public List<me.verni.transaction.Transaction> getTransactionsByUser(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Transaction t WHERE t.user.id = :userId";
            Query<me.verni.transaction.Transaction> query = session.createQuery(hql, me.verni.transaction.Transaction.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    public void deleteTransaction(Long id) {
        Transaction dbTransaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            dbTransaction = session.beginTransaction();


            me.verni.transaction.Transaction transaction = session.get(me.verni.transaction.Transaction.class, id);
            if (transaction == null) {
                throw new IllegalArgumentException("Transaction not found with ID: " + id);
            }

            session.delete(transaction);
            dbTransaction.commit();
        } catch (Exception e) {
            if (dbTransaction != null) dbTransaction.rollback();
            throw e;
        }
    }
}