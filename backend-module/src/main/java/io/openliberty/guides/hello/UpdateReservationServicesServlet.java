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

@WebServlet("/UpdateReservationServices")
public class UpdateReservationServicesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BufferedReader reader = request.getReader();
        String line = reader.readLine();
        reader.close();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(line);

        try {
            // Update ReservationService table
            if (root.isArray()) {
                for (JsonNode reservationServiceNode : root) {
                    DatabaseUtil.Update(reservationServiceNode, "ReservationService", "ReservationID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
