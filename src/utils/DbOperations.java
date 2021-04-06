package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbOperations {

    public static Connection connectToDb() {

        String DB_URL = "jdbc:mysql://localhost/lab2";
        String USER = "root";
        String PASS = "";
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void disconnectFromDb(Connection connection, Statement statement) {
        try {
            if (connection != null && statement != null) {
                connection.close();
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
