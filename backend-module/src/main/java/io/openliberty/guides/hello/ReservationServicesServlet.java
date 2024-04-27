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

import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/Reservations/GetReservationServices")
public class ReservationServicesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("Get for Reservation Services Data");
        try {
            // Retrieving data from the server (e.g., from the database)
            ResultSet resultSet = DatabaseUtil.Selecet("ReservationService");

            List<Map<String, Object>> rows = new ArrayList<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> rowData = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    rowData.put(columnName, value);
                }
                rows.add(rowData);
            }

            // Converting data to a JSON string
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(rows);

            // Setting content type and sending the JSON string as a response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonString);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handling the error
        }
    }
}
