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

    // Método para obter todas as transações de um usuário
    public List<Transaction> getTransactionsByUserId(int userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "FROM Transaction T WHERE T.user.id = :userId ORDER BY T.transactionDate DESC";
            return session.createQuery(hql, Transaction.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    // Método para fazer transferência
    public boolean transfer(String cpfFrom, String cpfTo, double amount) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        org.hibernate.Transaction tx = null;

        try {
            tx = session.beginTransaction();

            // Buscar o user pelo cpf
            UserDAO userDAO = new UserDAO();
            User userFrom = userDAO.getUserByCpf(cpfFrom);
            User userTo = userDAO.getUserByCpf(cpfTo);

            // Verificar se os usuários existem
            if (userFrom == null) {
                System.out.println("Sender user not found. Please check the CPF.");
                return false;
            }
            if (userTo == null) {
                System.out.println("Recipient user not found. Please check the CPF.");
                return false;
            }

            // Verificar o saldo do remetente
            double currentBalance = getLastBalanceByUserId(userFrom.getId());
            if (currentBalance < amount) {
                System.out.println("Insufficient funds");
                return false;
            }

            // Atualizar saldo do remetente
            double newBalanceFrom = currentBalance - amount;
            Transaction transactionFrom = new Transaction();
            transactionFrom.setUser(userFrom);
            transactionFrom.setAmount(-amount);
            transactionFrom.setBalance(newBalanceFrom);
            transactionFrom.setType("Transfer Out");
            transactionFrom.setTransactionDate(new Date());
            session.save(transactionFrom);

            // Atualizar saldo do destinatário
            double currentBalanceTo = getLastBalanceByUserId(userTo.getId());
            double newBalanceTo = currentBalanceTo + amount;
            Transaction transactionTo = new Transaction();
            transactionTo.setUser(userTo);
            transactionTo.setAmount(amount);
            transactionTo.setBalance(newBalanceTo);
            transactionTo.setType("Transfer In");
            transactionTo.setTransactionDate(new Date());
            session.save(transactionTo);

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
