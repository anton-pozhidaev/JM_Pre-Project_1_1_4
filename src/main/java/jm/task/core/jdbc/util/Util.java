package jm.task.core.jdbc.util;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

    private static Logger log = Logger.getLogger(Util.class.getName());

    private static final String DB_URL = "jdbc:mysql://localhost:3306/jmtaskjdbc?serverTimezone=Europe/Moscow";
    private static final String USER = "root";
    private static final String PASSWORD = "nimda";
    private static final String DIALECT_mySQL = "org.hibernate.dialect.MySQL8Dialect";

    private static Connection dbConn;
    private static SessionFactory factory;

    private Util() {

    }


    public static Connection getConnection() {
        if (dbConn == null) {
            try {
                return dbConn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            } catch (SQLException e) {
                log.log(Level.SEVERE, "Creation of JDBC CONNECTION to DB is unsuccessful", e);
                e.printStackTrace();
                return null;
            }
        }
        return dbConn;
    }

    public static SessionFactory getSessionFactory() {
        if (factory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties settings = new Properties();

                settings.put(Environment.URL, DB_URL);
                settings.put(Environment.USER, USER);
                settings.put(Environment.PASS, PASSWORD);
                settings.put(Environment.DIALECT, DIALECT_mySQL);

//                settings.put(Environment.SHOW_SQL, "true");

                configuration.setProperties(settings);
                configuration.addAnnotatedClass(User.class);

                StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                factory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Throwable ex) {
                System.err.println("Failed to create sessionFactory object." + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
        return factory;
    }
}