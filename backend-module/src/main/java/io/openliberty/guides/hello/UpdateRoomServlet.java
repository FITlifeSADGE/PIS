package io.openliberty.guides.hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;

@WebServlet("/UpdateRoom")
public class UpdateRoomServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Načítanie údajov z požiadavky
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        String requestData = sb.toString();

        try {
            DatabaseUtil.Update(requestData, "Room", "RoomID");
            System.out.println("Received update for Room:");
            System.out.println(requestData);
        } 
        catch (SQLException e) {
            e.printStackTrace();
            // Spracovanie chyby
        }
    }
}