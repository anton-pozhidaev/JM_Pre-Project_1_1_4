package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private final SessionFactory factory = Util.getSessionFactory();
    private final static Logger log = Logger.getLogger(UserDaoHibernateImpl.class.getName());

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        Transaction tx = null;
        String sql = "CREATE TABLE IF NOT EXISTS users " +
                "(id INTEGER not NULL AUTO_INCREMENT, " +
                " name VARCHAR(50), " +
                " last_name VARCHAR (50), " +
                " age SMALLINT not NULL, " +
                "PRIMARY KEY (id))";

        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            Query query = session.createSQLQuery(sql);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Creating table \"users\" is unsuccessful...attempt to rollback transaction", e);
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction tx = null;
        String sql = "DROP TABLE IF EXISTS users";

        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            Query query = session.createSQLQuery(sql);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Droping of DB is unsuccessful", e);
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction tx = null;

        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.save(new User(name, lastName, age));
            tx.commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Saving new User is unsuccessful...attempt to rollback transaction", e);
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        System.out.println("User с именем – " + name + " добавлен в базу данных");
    }

    @Override
    public void removeUserById(long id) {
        Transaction tx = null;

        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            tx.commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Removing of user from DB is unsuccessful", e);
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }

    }

    @Override
    public List<User> getAllUsers() {
        Transaction tx = null;
        List<User> allUsersList = null;
        // String hql = "SELECT a FROM User a";
        String hql = "FROM User";

        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            org.hibernate.query.Query<User> query = session.createQuery(hql, User.class);
            allUsersList = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Getting of all users from DB is unsuccessful", e);
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return allUsersList;
    }

    @Override
    public void cleanUsersTable() {
        Transaction tx = null;
        String sql = "DELETE FROM users";

        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            Query query = session.createSQLQuery(sql);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Cleaning of DB \"users\" is unsuccessful", e);
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }
}
