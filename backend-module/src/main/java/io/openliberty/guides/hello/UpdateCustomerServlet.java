package io.openliberty.guides.hello;

import java.io.BufferedReader;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Calendar;
import com.fasterxml.jackson.databind.node.ObjectNode;

@WebServlet("/UpdateCustomer")
public class UpdateCustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BufferedReader reader = request.getReader();
        String line = reader.readLine();
        reader.close();
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(line);
        ((ObjectNode) root).put("dateOfBirth", Date(root.get("dateOfBirth")));
        System.out.println(root.get("dateOfBirth"));

        System.out.println(root);
        try 
        {
            DatabaseUtil.Update(root, "Person", "PersonID");
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}