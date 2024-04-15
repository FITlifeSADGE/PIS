package io.openliberty.guides.hello;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;


// @WebServlet("/pokus")
// public class PokusServlet extends HttpServlet {
//     private static final long serialVersionUID = 1L;

//     protected void safeChanges(HttpServletRequest request, HttpServletResponse response)
//             throws ServletException, IOException {
//         try {
//             ResultSet resultSet = DatabaseUtil.Selecet("Person");
//             String sql = "SELECT * FROM customers WHERE id = ?";

//             // Set parameter value (example: customer ID)
//             int customerId = 1;
//             preparedStatement.setInt(1, customerId);

//             // Execute the query
//             resultSet = preparedStatement.executeQuery();

//             // Process the query results
//             while (resultSet.next()) {
//                 int id = resultSet.getInt("id");
//                 String firstName = resultSet.getString("first_name");
//                 String lastName = resultSet.getString("last_name");
//                 String email = resultSet.getString("email");

//                 System.out.println("Customer ID: " + id);
//                 System.out.println("First Name: " + firstName);
//                 System.out.println("Last Name: " + lastName);
//                 System.out.println("Email: " + email);
//                 System.out.println();
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         } finally {
//             // Close JDBC objects in reverse order
//             try {
//                 if (resultSet != null) {
//                     resultSet.close();
//                 }
//                 if (preparedStatement != null) {
//                     preparedStatement.close();
//                 }
//                 if (connection != null) {
//                     connection.close();
//                 }
//             } catch (SQLException e) {
//                 e.printStackTrace();
//             }
//         }
//     }
// }


@WebServlet("/pokus")
public class PokusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {  

        try {
            // Získanie údajov zo servera (napr. z databázy)
            ResultSet resultSet = DatabaseUtil.Selecet("Person");

            List<Map<String, Object>> rows = new ArrayList<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) 
            {
                Map<String, Object> rowData = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) 
                {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    rowData.put(columnName, value);
                }
                rows.add(rowData);
            }

            // Konverzia údajov na JSON reťazec
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(rows);

            // Nastavenie typu obsahu a odoslanie JSON reťazca ako odpoveď
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonString);

        } catch (SQLException e) {
            e.printStackTrace();
            // Spracovanie chyby
        }
    }
}
