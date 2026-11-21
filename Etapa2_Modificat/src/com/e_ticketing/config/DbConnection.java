package com.e_ticketing.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/e_ticketing";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static Connection connection;


    private DbConnection() {}

    public static Connection getInstance() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
