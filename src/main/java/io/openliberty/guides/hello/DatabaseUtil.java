package io.openliberty.guides.hello;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


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
    public static void initializeDatabase() throws SQLException, IOException {
        try (Connection connection = getConnection()) {
            String sqlScript = new String(Files.readAllBytes(Paths.get("/mnt/c/Users/lukas/projects/school_projects/1mit/PIS/src/main/resources/db/init.sql")));
            Statement statement = connection.createStatement();
            String[] sqlCommands = sqlScript.split(";");
            for (String sqlCommand : sqlCommands) {
                sqlCommand = sqlCommand.trim(); // Odstranění bílých znaků ze začátku a konce řetězce
                if (!sqlCommand.isEmpty()) { // Ignorovat prázdné příkazy
                    statement.execute(sqlCommand);
                }
            }
        }
    }
}
