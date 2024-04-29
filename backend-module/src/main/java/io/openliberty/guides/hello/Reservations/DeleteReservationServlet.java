package io.openliberty.guides.hello.Reservations;

import java.io.BufferedReader;
import java.io.IOException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.openliberty.guides.hello.model.Reservation;
import io.openliberty.guides.hello.model.ReservationService;

@WebServlet("/Reservations/DeleteReservation")
public class DeleteReservationServlet extends HttpServlet {
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

        try{
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


            //find reservation by id
            TypedQuery<Reservation> query = em.createNamedQuery("Reservation.findById", Reservation.class);
            query.setParameter("id", root.path("ReservationID").asInt());
            Reservation reservation = query.getSingleResult();

            //remove reservation from db
            em.getTransaction().begin();
            em.remove(reservation);
            em.getTransaction().commit();

            System.out.println("Deleted reservation: " + reservation);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Reservation not found");
        } finally {
            em.close();
            emf.close();
        }

    }

}


    //     try {
    //         // Delete associated services first
    //         if (root.has("ReservationID")) {
    //             DatabaseUtil.Delete2(root, "ReservationService", "ReservationID");
    //         }

    //         // Then delete the reservation itself
    //         DatabaseUtil.Delete(root, "Reservation", "ReservationID");
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }

