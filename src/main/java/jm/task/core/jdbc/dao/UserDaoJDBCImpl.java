package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection connection = Util.getConnection();
    private final static Logger log = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users " +
                "(id INTEGER not NULL AUTO_INCREMENT, " +
                " name VARCHAR(50), " +
                " lastname VARCHAR (50), " +
                " age SMALLINT not NULL, " +
                "PRIMARY KEY (id))";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Creation of DB is unsuccessful", e);
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Droping of DB is unsuccessful", e);
        }
    }

    public void saveUser(String name1, String lastName1, byte age1) {
        String sql = "INSERT INTO users (name, lastname, age) VALUES (?, ?, ?)";
        String sql2 = "SELECT name, lastname FROM users WHERE name=? AND lastname=?";

        try (PreparedStatement pStmnt = connection.prepareStatement(sql);
        PreparedStatement pStmnt2 = connection.prepareStatement(sql2)) {
            connection.setAutoCommit(false);
            pStmnt.setString(1,name1);
            pStmnt.setString(2,lastName1);
            pStmnt.setByte(3, age1);
            pStmnt.executeUpdate();

            pStmnt2.setString(1, name1);
            pStmnt2.setString(2, lastName1);
            ResultSet rSet = pStmnt2.executeQuery();
            while (rSet.next()) {
                String uName = rSet.getString("name");
                String uLastName = rSet.getString("lastname");
                System.out.println("User с именем \"" + uName + " " + uLastName +"\" 100пудова добавлен в базу данных");
            }
            connection.commit();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Saving of user to DB is unsuccessful", e);
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back\n");
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id=?";

        try (PreparedStatement pStmnt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            pStmnt.setLong(1,id);
            pStmnt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Removing of user from DB is unsuccessful", e);
        }

    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> allUsersList = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet rSet = statement.executeQuery(sql)) {
            while (rSet.next()) {
                Long id = rSet.getLong("id");
                String name = rSet.getString("name");
                String lastName = rSet.getString("lastName");
                Byte age = rSet.getByte("age");
                allUsersList.add(new User(id, name, lastName, age));
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Getting of all users from DB is unsuccessful", e);
        }
        return allUsersList;
    }

    public void cleanUsersTable() {
        String sql = "DELETE FROM users";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Cleaning of DB \"users\" is unsuccessful", e);
        }
    }
}
