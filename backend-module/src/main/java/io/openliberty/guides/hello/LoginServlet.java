package io.openliberty.guides.hello;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.sql.ResultSet;

import javax.json.Json;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.openliberty.guides.hello.model.LoginRequest;

import io.openliberty.guides.hello.model.Person;

// Hash
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

// JWT
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;


@WebServlet("/loginVerify")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { 
           
        // JPA
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-hibernate-mysql");
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> query = em.createNamedQuery("Person.findById", Person.class);
            query.setParameter("id", 1);
            Person person = query.getSingleResult();
            
            System.out.println(person);
            
        } catch (NoResultException e) {
            // User not found
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("User not found");
        } finally {
            em.close();
            emf.close();
        }


        // JPA

        ObjectMapper mapper = new ObjectMapper();
        LoginRequest loginRequest = mapper.readValue(request.getInputStream(), LoginRequest.class);
        String username = loginRequest.getUsername();
        String password = hashPassword(loginRequest.getPassword());

        if (username != null && password != null) {
            try {
                Connection connection = DatabaseUtil.getConnection();
                String query = "SELECT * FROM Employee Join Person on Employee.EmployeeID = Person.PersonID Where Email=?;";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                
                if (resultSet.next()) {
                    String dbPassword = resultSet.getString("Password"); // Získat heslo z databáze
                    String dbRole = resultSet.getString("Assignment"); // Získat heslo z databáze
                    if (password.equals(dbPassword)) {
                        // User found and password matched, login successful

                        // Generate JWT token
                        Algorithm algorithm = Algorithm.HMAC256("abcdefg");
                        String token = JWT.create()
                        .withIssuer("PIS")
                        .withClaim("username", username)
                        .withClaim("role", dbRole)
                        .withIssuedAt(new Date())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 8 * 60 * 60 * 1000)) // expire in 8 hours
                        .sign(algorithm);

                        // Send token to client
                        String jsonString = Json.createObjectBuilder()
                            .add("token", token)
                            .build()
                            .toString();

                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(jsonString);
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

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 32) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}