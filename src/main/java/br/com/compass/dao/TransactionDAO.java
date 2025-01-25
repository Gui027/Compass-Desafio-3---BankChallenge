package br.com.compass.dao;

import br.com.compass.model.Transaction;
import br.com.compass.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class TransactionDAO {

    // Método para salvar uma transação
    public boolean saveTransaction(Transaction transaction) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        org.hibernate.Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(transaction);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    // Método para recuperar todas as transações de um usuário
    public List<Transaction> getTransactionsByUserId(int userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            return session.createQuery("FROM Transaction WHERE user.id = :userId", Transaction.class)
                    .setParameter("userId", userId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    // Método para recuperar transações específicas, por exemplo, por tipo de transação
    public List<Transaction> getTransactionsByType(String transactionType) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            return session.createQuery("FROM Transaction WHERE transactionType = :transactionType", Transaction.class)
                    .setParameter("transactionType", transactionType)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
}
