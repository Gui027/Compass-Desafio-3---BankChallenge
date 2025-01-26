package br.com.compass.dao;

import br.com.compass.model.Transaction;
import br.com.compass.model.User;
import br.com.compass.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Date;
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

    // Método para fazer o depósito
    public boolean deposit(int userId, double amount) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        org.hibernate.Transaction tx = null;

        try {
            tx = session.beginTransaction();

            // Buscar o saldo atual
            double currentBalance = getLastBalanceByUserId(userId);

            // Calcular o novo saldo
            double newBalance = currentBalance + amount;

            // Criar uma nova transação
            Transaction transaction = new Transaction();
            transaction.setUser(session.get(User.class, userId)); // Associar ao usuário
            transaction.setAmount(amount);
            transaction.setBalance(newBalance);
            transaction.setType("Deposit"); // Tipo de transação
            transaction.setTransactionDate(new Date()); // Data atual

            // Salvar a transação
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

    // Método para fazer o saque
    public boolean withdraw(int userId, double amount) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        org.hibernate.Transaction tx = null;

        try {
            tx = session.beginTransaction();

            // Buscar o saldo atual
            double currentBalance = getLastBalanceByUserId(userId);

            // Calcular o novo saldo
            double newBalance = currentBalance - amount;

            // Criar uma nova transação
            Transaction transaction = new Transaction();
            transaction.setUser(session.get(User.class, userId));
            transaction.setAmount(amount);
            transaction.setBalance(newBalance);
            transaction.setType("Withdraw");
            transaction.setTransactionDate(new Date());

            // Salvar a transação
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

}
