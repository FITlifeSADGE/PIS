package io.openliberty.guides.hello;

import java.io.IOException;
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
import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;


@WebServlet("/Customers/GetServices")
public class CustomerDetailServicesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {  

        
        // Koukne jestli existuje session pro uživatele
        HttpSession session = request.getSession(false);
        if (session == null) {
            // Jinak vrátí na login page
            System.out.println("Session not found");
            response.sendRedirect("/Home/login");
            return;
        }

        System.out.println("Get for Service Data");
        try {
            // Získanie údajov zo servera (napr. z databázy)
            ResultSet resultSet = DatabaseUtil.Selecet("Service");

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
