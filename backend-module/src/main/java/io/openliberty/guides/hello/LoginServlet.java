package io.openliberty.guides.hello;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.openliberty.guides.hello.model.LoginRequest;


@WebServlet("/loginVerify")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {  
        ObjectMapper mapper = new ObjectMapper();
        LoginRequest loginRequest = mapper.readValue(request.getInputStream(), LoginRequest.class);
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        // System.out.println(username);
        if (username != null && password != null) {
            try {
                Connection connection = DatabaseUtil.getConnection();
                String query = "SELECT * FROM Employee Join Person on Employee.EmployeeID = Person.PersonID Where Email=?;";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                
                if (resultSet.next()) {
                    String dbPassword = resultSet.getString("Password"); // Získat heslo z databáze
                    if (password.equals(dbPassword)) {
                        // User found and password matched, login successful
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("Login successful");
                    } else {
                        // Password incorrect
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Incorrect password");
                    }
                } else {
                    // User not found
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("User not found");
                }
                
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Interní chyba serveru
                response.getWriter().write("Error: Unable to process login");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Chybějící nebo neplatné údaje
            response.getWriter().write("Error: Username or password not provided");
        }
    }
}

// public class CustomerFetcher {
//     public static void main(String[] args) {
//         try {
//             Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
//             Statement statement = connection.createStatement();
            
//             // Execute SQL Query
//             ResultSet resultSet = statement.executeQuery("SELECT * FROM Customer");

//             // Process the ResultSet (retrieve customer data)
//             while (resultSet.next()) {
//                 int customerID = resultSet.getInt("CustomerID");
//                 String address = resultSet.getString("Address");
//                 boolean subscription = resultSet.getBoolean("Subscription");

//                 // Process or display the customer data as needed
//                 System.out.println("Customer ID: " + customerID);
//                 System.out.println("Address: " + address);
//                 System.out.println("Subscription: " + (subscription ? "Subscribed" : "Not Subscribed"));
//             }

//             // Close resources
//             resultSet.close();
//             statement.close();
//             connection.close();

//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
// }
