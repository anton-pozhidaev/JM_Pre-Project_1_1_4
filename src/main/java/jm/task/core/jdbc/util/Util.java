package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//  jdbc:mysql://localhost:3306/springTest?serverTimezone=Europe/Moscow

public class Util implements AutoCloseable {

    private final String DB_URL;
    private final String USER;
    private final String PASSWORD;
    private Connection dbConn;

    private Util() {
        this.DB_URL = "jdbc:mysql://localhost:3306/jmtaskjdbc?useTimezone=true&serverTimezone=UTC";
        this.USER = "root";
        this.PASSWORD = "nimda";
        this.dbConn = null;
    }

    public static Util getInstance() {
        return new Util();
    }

    public Connection getConnection() throws SQLException {
        return dbConn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    @Override
    public void close() throws SQLException {
        dbConn.close();
    }

    /*
    public static Connection getConnectionToMyDB() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
*/

}
