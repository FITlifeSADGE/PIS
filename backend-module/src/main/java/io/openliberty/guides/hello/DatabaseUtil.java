package io.openliberty.guides.hello;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;


// Tato třída bude obsahovat metodu pro připojení k databázi
public class DatabaseUtil {
    public static Connection getConnection() throws SQLException 
    {
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
    public static void initializeDatabase() throws SQLException, IOException 
    {
        try (Connection connection = getConnection()) {
            InputStream inputStream = DatabaseUtil.class.getResourceAsStream("/db/init.sql");
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
            }
            String sqlScript = stringBuilder.toString();
            Statement statement = connection.createStatement();
            String[] sqlCommands1 = sqlScript.split(";");

            for (String sqlCommand : (sqlCommands1)) {
                sqlCommand = sqlCommand.trim(); // Odstranění bílých znaků ze začátku a konce řetězce
                if (!sqlCommand.isEmpty()) { // Ignorovat prázdné příkazy
                    statement.execute(sqlCommand);
                }
            }

            InputStream inputStream2 = DatabaseUtil.class.getResourceAsStream("/db/init_data.sql");
            StringBuilder stringBuilder2 = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream2))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder2.append(line).append("\n");
                }
            }
            String sqlScript2 = stringBuilder2.toString();
            String[] sqlCommands2 = sqlScript2.split(";");

            for (String sqlCommand : (sqlCommands2)) {
                sqlCommand = sqlCommand.trim(); // Odstranění bílých znaků ze začátku a konce řetězce
                if (!sqlCommand.isEmpty()) { // Ignorovat prázdné příkazy
                    statement.execute(sqlCommand);
                }
            }

        }
    }

    public static ResultSet Selecet(String Enitity) throws SQLException, IOException 
    {
        Connection connection = getConnection();

        // Definujte SQL dotaz
        String sqlQuery = "SELECT * FROM " + Enitity;
        
        // Pripravte príkaz na vykonanie dotazu
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        
        // Vykonajte dotaz a získajte výsledky
        ResultSet resultSet = preparedStatement.executeQuery();
        
        // Spracovanie výsledkov dotazu
        return resultSet;
    }
}
