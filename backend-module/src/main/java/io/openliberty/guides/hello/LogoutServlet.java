package io.openliberty.guides.hello;

import java.io.IOException;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {   
       
        // Koukne jestli existuje session pro uživatele
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Zruší session
            session.invalidate();
        }
        System.out.println("Session destroyed, redirecting to login page");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}