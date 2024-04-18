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

import com.fasterxml.jackson.databind.JsonNode;

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

    public static void update(String requestData, String Enitity, String ID) throws SQLException, IOException 
    {
        try (Connection connection = getConnection()) {

        StringBuilder sqlQuery = new StringBuilder("UPDATE " + Enitity + " SET ");

        requestData = requestData.replace("}", "").replace("{", "");
        String[] dataPairs = requestData.split(",");
        for (int i = 0; i < dataPairs.length; i++) {
            String pair = dataPairs[i];
            String[] keyValue = pair.split(":");
            String columnName = keyValue[0].replaceAll("\"", "");
            String columnValue = keyValue[1];
        
            // Přidání sloupce a hodnoty do SQL dotazu
            if (!columnName.equals("editable"))
            {    
                sqlQuery.append(columnName).append("=").append(columnValue).append(",");
            }
        }
        sqlQuery.deleteCharAt(sqlQuery.length() - 1);

        //Hladanie ID
        String[] keyValuePairs = requestData.split(",");
        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split(":");

            if (keyValue[0].equals("\"" + ID + "\"")) {

                sqlQuery.append(" WHERE ServiceID = ").append(keyValue[1]).append(";"); // Odstránenie úvodzoviek
            }
        }
        System.out.println("Generated SQL query:");
        System.out.println(sqlQuery.toString());

        // Pripravte príkaz na vykonanie dotazu
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery.toString());
        preparedStatement.executeUpdate();
        }
    
    }
    
    public static void Update(JsonNode root, String Enitity, String ID) throws SQLException, IOException 
    {
        System.out.println("Upadate for "+ Enitity);
        try (Connection connection = getConnection()) {
        
        //ziskanie nazvu stlpcov pre danu entitu
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '"+ Enitity +"';");
        ResultSet NameOfColumns = preparedStatement.executeQuery();

        StringBuilder sqlQuery = new StringBuilder("UPDATE " + Enitity + " SET ");

        while (NameOfColumns.next()) {
            String columnName = NameOfColumns.getString("COLUMN_NAME");
            String jsonValue = root.path(columnName).asText(); root.path(columnName);

            if (!jsonValue.matches("-?\\d+(\\.\\d+)?"))  // Ak sa nejedna o cislo
                sqlQuery.append(columnName).append("=").append("\"" + jsonValue + "\"").append(",");
            else
                sqlQuery.append(columnName).append("=").append(jsonValue).append(",");
        }
        sqlQuery.deleteCharAt(sqlQuery.length() - 1); //odstranenie podslednej ciarky
        sqlQuery.append(" WHERE "+ ID + " = ").append(root.path(ID).asText()).append(";");

        PreparedStatement query = connection.prepareStatement(sqlQuery.toString());
        query.executeUpdate();

        NameOfColumns.close();
        preparedStatement.close();
        connection.close();
        }
    }
}
