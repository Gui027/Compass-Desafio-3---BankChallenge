package br.com.compass.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static void initializeDatabase() {
        getSessionFactory();
        System.out.println("Database initialized.");
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");
                sessionFactory = configuration.buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
                throw new ExceptionInInitializerError("Error initializing Hibernate.");
            }
        }
        return sessionFactory;
    }
}
