package io.openliberty.guides.hello.Reservations;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



import io.openliberty.guides.hello.model.Reservation;

@WebServlet("/Reservations/AddReservation")
public class AddReservationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

BufferedReader reader = request.getReader();
String line = reader.readLine();
reader.close();

ObjectMapper mapper = new ObjectMapper();
JsonNode root = mapper.readTree(line);

DateFormat formatter = new SimpleDateFormat("HH:mm");
Time comingTime;
Time leavingTime;

EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-hibernate-mysql");
EntityManager em = emf.createEntityManager();
try {
    // generate a unique ID for the reservation
    TypedQuery<Integer> query = em.createQuery("SELECT MAX(r.id) FROM Reservation r", Integer.class);
    Integer maxId = query.getSingleResult();
    int newReservationId = maxId != null ? maxId + 1 : 1;

    // create a new reservation
    Reservation reservation = new Reservation();

    // Make END and START to type Date
    Date start = Date.valueOf(root.path("Start").asText());
    Date end = Date.valueOf(root.path("End").asText());

    // print string value of cost
    System.out.println(root.path("Cost").asText());

    //change string to float
    float cost = Float.parseFloat(root.path("Cost").asText());
    
    // parse comingTime and leavingTime to java.sql.Time
    try {
        comingTime = new Time(formatter.parse(root.path("ComingTime").asText()).getTime());
        leavingTime = new Time(formatter.parse(root.path("LeavingTime").asText()).getTime());
    } catch (ParseException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("Invalid time format");
        return;
    }

    // populate reservation parameters
    reservation.setReservationId(newReservationId);
    reservation.setCustomerID(root.path("CustomerID").asInt());
    reservation.setRoomID(root.path("RoomID").asInt());
    reservation.setStart(start);
    reservation.setEnd(end);
    reservation.setState("Pending");
    reservation.setCost(cost);
    reservation.setCommingTime(comingTime);
    reservation.setLeavingTime(leavingTime);
    reservation.setBusinessGuest(root.path("BusinessGuest").asBoolean());
    reservation.setParking(root.path("Parking").asBoolean());
    
    // print reservation
    System.out.println("Reservation was created");
    System.out.println(reservation);


    // send reservation to db
    em.getTransaction().begin();
    em.persist(reservation);
    em.getTransaction().commit();

    System.out.println("Reservation was created");

      // Create a response payload with the newly created reservation
      ObjectMapper responseMapper = new ObjectMapper();
      String responsePayload = responseMapper.writeValueAsString(reservation);

      // Set the response status and write the response payload
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().write(responsePayload);

    

} catch (NoResultException e) {
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    response.getWriter().write("Reservation was not created");
} finally {
    em.close();
    emf.close();
}
}
}