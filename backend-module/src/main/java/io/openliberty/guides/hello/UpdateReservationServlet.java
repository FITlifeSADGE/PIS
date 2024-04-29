package io.openliberty.guides.hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.text.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.openliberty.guides.hello.model.Reservation;
import io.openliberty.guides.hello.model.ReservationService;
import io.openliberty.guides.hello.model.Service;

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

        System.out.println("Received JSON data: " + line);

        DateFormat formatter = new SimpleDateFormat("HH:mm");
Time comingTime;
Time leavingTime;

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-hibernate-mysql");
        EntityManager em = emf.createEntityManager();

        try {
            // Find reservation by id
            TypedQuery<Reservation> query = em.createNamedQuery("Reservation.findById", Reservation.class);
            query.setParameter("id", root.path("ReservationID").asInt());
            Reservation reservation = query.getSingleResult();

            System.out.println("Retrieved reservation: " + reservation);
            System.out.println("Updating reservation...");

            // parse comingTime and leavingTime to java.sql.Time
    try {
        comingTime = new Time(formatter.parse(root.path("CommingTime").asText()).getTime());
        leavingTime = new Time(formatter.parse(root.path("LeavingTime").asText()).getTime());
    } catch (ParseException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("Invalid time format");
        return;
    }
            // Update reservation parameters
            // Parse JSON data
            Date startDate = Date.valueOf(root.path("Start").asText());
            Date endDate = Date.valueOf(root.path("End").asText());
            float cost = Float.parseFloat(root.path("Cost").asText());
            String state = root.path("State").asText();
            boolean businessGuest = root.path("BusinessGuest").asInt() == 1;
            boolean parking = root.path("Parking").asInt() == 1;

            // Call updateReservation method
            reservation.updateReservation(startDate, endDate, cost, state, businessGuest, parking, comingTime, leavingTime );

            // Send reservation to db
            em.getTransaction().begin();
            em.merge(reservation);
            System.out.println("Committing transaction...");
            em.getTransaction().commit();
            System.out.println("Transaction committed successfully.");

            System.out.println("Updated reservation: " + reservation);

            // find services by reservation id
            TypedQuery<ReservationService> query2 = em.createNamedQuery("ReservationService.findByReservationId",
                    ReservationService.class);
            query2.setParameter("reservationId", root.path("ReservationID").asInt());
            List<ReservationService> reservationServices = query2.getResultList();

            System.out.println("Retrieved reservation services: " + reservationServices);

            // remove all services associated with reservation
            for (ReservationService rs : reservationServices) {
                em.getTransaction().begin();
                em.remove(rs);
                em.getTransaction().commit();
                System.out.println("Deleted reservation service: " + rs);
            }

            // now add services to reservation by ServiceIDs
            JsonNode services = root.path("ServiceIDs");
            for (JsonNode service : services) {

                // Fetch the associated Service object from the database
                Service serviceObj = em.find(Service.class, service.asInt());
                //print service object
                System.out.println("Service object: " + serviceObj);


                ReservationService rs = new ReservationService(root.path("ReservationID").asInt(),
                        service.asInt() );

                // Set the Reservation object to the ReservationService
                rs.setReservation(reservation);
                // Set the Service object to the ReservationService
                rs.setService(serviceObj);
    
                // Persist the ReservationService
                 em.getTransaction().begin();
                em.persist(rs);
                em.getTransaction().commit();
                System.out.println("Added reservation service: " + rs);

            }

             

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating reservation: " + e.getMessage());
        } finally {
            em.close();
            emf.close();
        }
    }
}