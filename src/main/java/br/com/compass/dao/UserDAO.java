package br.com.compass.dao;

import br.com.compass.model.User;
import br.com.compass.util.HibernateUtil;
import org.hibernate.Session;

public class UserDAO {

    public boolean saveUser(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();

            return true;
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
}
