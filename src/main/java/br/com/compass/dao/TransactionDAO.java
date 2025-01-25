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

    // Método para ver o saldo
    public double getLastBalanceByUserId(int userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // Consultar a última transação do usuário com o saldo mais recente
            String hql = "SELECT T.balance FROM Transaction T WHERE T.user.id = :userId ORDER BY T.transactionDate DESC";
            List<Double> result = session.createQuery(hql, Double.class)
                                         .setParameter("userId", userId)
                                         .setMaxResults(1) // Pega apenas a última transação
                                         .getResultList();
            
            if (!result.isEmpty()) {
                return result.get(0); // Retorna o saldo da última transação
            } else {
                return 0.0; // Caso não haja transações, saldo é 0
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        } finally {
            session.close();
        }
    }
    
}
