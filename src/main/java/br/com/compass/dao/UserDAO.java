package br.com.compass.dao;

import br.com.compass.model.User;
import br.com.compass.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDAO {

    public boolean saveUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}
