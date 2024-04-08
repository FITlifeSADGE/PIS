package io.openliberty.guides.hello;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Tato třída bude obsahovat metodu pro připojení k databázi
public class DatabaseUtil {
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new SQLException("MySQL JDBC Driver nenalezen.", e);
        }
        String jdbcUrl = "jdbc:mysql://localhost:3306/test_db";
        String username = "michal";
        String password = "average";
        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}
