package io.openliberty.guides.hello;

import java.io.BufferedReader;
import java.io.IOException;

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

import java.sql.SQLException;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.openliberty.guides.hello.model.Room;

@WebServlet("/AddRoom")
public class AddRoomServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BufferedReader reader = request.getReader();
        String line = reader.readLine();
        reader.close();
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(line);


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-hibernate-mysql");
        EntityManager em = emf.createEntityManager();
        try {
            //create and update new room 
            Room room = new Room();

            //get the biggest Roomid
            TypedQuery<Integer> query = em.createNamedQuery("Room.findMaxId", Integer.class);
            int newid = query.getSingleResult();

            room.setRoomId(newid+1);
            room.updateRoom(
                root.path("TypeRoom").asText(), 
                root.path("Cost").floatValue(),  
                root.path("Equip").asText(),  
                root.path("State").asText(),  
                root.path("Beds").asInt()
                );

            em.getTransaction().begin();    //start transakcion
            em.persist(room);               //send data to db
            em.getTransaction().commit();   //end transakcion
        
            System.out.println("Room was created");
            
        } catch (NoResultException e) {             
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Room was not created");
        } finally {
            em.close();
            emf.close();
        }





        
        // try 
        // {
        //     DatabaseUtil.Add(root, "Room", "RoomID");
        // } 
        // catch (SQLException e) {
        //     e.printStackTrace();
        // }
    }
}