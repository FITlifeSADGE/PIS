package io.openliberty.guides.hello;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.openliberty.guides.hello.model.Service;


@WebServlet("/Services/GetServices")
public class ServicesServlet extends HttpServlet {
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
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-hibernate-mysql");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<Service> query = em.createNamedQuery("Service.allRows", Service.class);
        List<Service> services = query.getResultList();
        
        // Transform data 
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Service service : services) {
            Map<String, Object> rowData = new HashMap<>();
            rowData.put("ServiceID", service.getServiceId());
            rowData.put("Name", service.getName());
            rowData.put("Cost", service.getCost());
            rowData.put("Availability", service.getAvailability());
            rowData.put("Description", service.getDescription());

            rows.add(rowData);
        }
        
        // Conversion data to json
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(rows);

        // Set type and send data to page
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonString);

    }
}