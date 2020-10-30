package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {

    private Util daoUtil = Util.getInstance();
    private static Logger log = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users " +
                "(id INTEGER not NULL AUTO_INCREMENT, " +
                " name VARCHAR(50), " +
                " lastname VARCHAR (50), " +
                " age SMALLINT not NULL, " +
                "PRIMARY KEY (id))";

        try (Connection connection = daoUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Creation of DB is unsuccessful", e);
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";

        try (Connection connection = daoUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Droping of User's DB is unsuccessful", e);
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";

        try (Connection connection = daoUtil.getConnection();
             PreparedStatement pStmnt = connection.prepareStatement(sql)) {
            pStmnt.setString(1,name);
            pStmnt.setString(2,lastName);
            pStmnt.setByte(3, age);
            pStmnt.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Saving of user to DB is unsuccessful", e);
            e.printStackTrace();
        }
        System.out.println("User с именем – " + name + " добавлен в базу данных");
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id=?";

        try (Connection connection = daoUtil.getConnection();
             PreparedStatement pStmnt = connection.prepareStatement(sql)) {
            pStmnt.setLong(1,id);
            pStmnt.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Removing of user from DB is unsuccessful", e);
            e.printStackTrace();
        }

    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> allUsersList = new ArrayList<>();

        try (Connection connection = daoUtil.getConnection();
             Statement statement = connection.createStatement();
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
            e.printStackTrace();
        }
        return allUsersList;
    }

    public void cleanUsersTable() {
        String sql = "DELETE FROM users";

        try (Connection connection = daoUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Cleaning of DB \"users\" is unsuccessful", e);
            e.printStackTrace();
        }
    }
}
