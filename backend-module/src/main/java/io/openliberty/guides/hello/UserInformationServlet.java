package io.openliberty.guides.hello;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



@WebServlet("/UserInformation")
public class UserInformationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
                
        HttpSession session = request.getSession(false);
        if (session == null) {
            // Jinak vrátí na login page
            System.out.println("Session not found");
            response.sendRedirect("/Home/login");
            return;
        }

        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("role", role);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(map);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonString);
        response.setStatus(HttpServletResponse.SC_OK);

        System.out.println(jsonString);
    

    }
}
