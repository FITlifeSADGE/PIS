package io.openliberty.guides.hello;

import java.io.BufferedReader;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/UpdateReservation")
public class UpdateReservationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    BufferedReader reader = request.getReader();
    String line = reader.readLine();
    reader.close();

    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(line);

    try {
        // Log the reservation data before update
        System.out.println("Reservation data before update:");
        System.out.println(root.toString());

        // Update Reservation table
        DatabaseUtil.Update(root, "Reservation", "ReservationID");

        // Update ReservationService table
        if (root.has("ServiceIDs")) {
            JsonNode serviceIDsNode = root.get("ServiceIDs");
            if (serviceIDsNode.isArray()) {
                // Construct an array of service IDs
                int[] serviceIDs = new int[serviceIDsNode.size()];
                for (int i = 0; i < serviceIDsNode.size(); i++) {
                    serviceIDs[i] = serviceIDsNode.get(i).asInt();
                }
                // Call Update2 method with reservation ID and array of service IDs
                DatabaseUtil.Update2(root, "ReservationService", "ReservationID", serviceIDs);
            }
        }

        // Log the reservation data after update
        System.out.println("Reservation data after update:");
        System.out.println(root.toString());

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}