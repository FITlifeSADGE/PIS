package io.openliberty.guides.hello.model;

import jakarta.persistence.*;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Entity
@NamedQuery(name = "Reservation.findById", query = "SELECT r FROM Reservation r WHERE r.reservationId = :id")
@NamedQuery(name = "Reservation.allRows", query = "SELECT r FROM Reservation r")
@NamedQuery(name = "Reservation.findMaxId", query = "SELECT MAX(r.reservationId) FROM Reservation r")
@NamedQuery(name = "Reservation.findByState", query = "SELECT r FROM Reservation r WHERE r.state = :state")
@NamedQuery(name = "Reservation.findByDate", query = "SELECT r FROM Reservation r WHERE r.start = :date")
@NamedQuery(name = "Reservation.findByBusinessGuest", query = "SELECT r FROM Reservation r WHERE r.businessGuest = :businessGuest")
@NamedQuery(name = "Reservation.findByParking", query = "SELECT r FROM Reservation r WHERE r.parking = :parking")
@NamedQuery(name = "Reservation.findByCost", query = "SELECT r FROM Reservation r WHERE r.cost = :cost")
@Table(name = "Reservation")
public class Reservation {

    @Id
    @Column(name = "ReservationID")
    private int reservationId;

    @Temporal(TemporalType.DATE)
    @Column(name = "Start")
    private Date start;

    @Temporal(TemporalType.DATE)
    @Column(name = "End")
    private Date end;

    @Column(name = "State")
    private String state;

    @Column(name = "Cost")
    private float cost;

    @Column(name = "CommingTime")
    private Time commingTime;

    @Column(name = "LeavingTime")
    private Time leavingTime;

    @Column(name = "BusinessGuest")
    private boolean businessGuest;

    @Column(name = "Parking")
    private boolean parking;

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Time getCommingTime() {
        return commingTime;
    }
    public void setCommingTime(Time commingTime) {
        this.commingTime = commingTime;
    }

    public Time getLeavingTime() {
        return leavingTime;
    }

    public void setLeavingTime(Time leavingTime) {
        this.leavingTime = leavingTime;
    }

    public boolean isBusinessGuest() {
        return businessGuest;
    }

    public void setBusinessGuest(boolean businessGuest) {
        this.businessGuest = businessGuest;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public void updateReservation(Date start, Date end, float cost, String state, boolean businessGuest, boolean parking) {
        this.start = start;
        this.end = end;
        this.cost = cost;
        this.state = state;
        this.businessGuest = businessGuest;
        this.parking = parking;
    }

    public void removeServices(EntityManager em) {
        em.createQuery("DELETE FROM ReservationService rs WHERE rs.id.reservationId = :reservationId")
          .setParameter("reservationId", this.reservationId)
          .executeUpdate();
    }
    


    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", start=" + start +
                ", end=" + end +
                ", state='" + state + '\'' +
                ", cost=" + cost +
                ", commingTime=" + commingTime +
                ", leavingTime=" + leavingTime +
                ", businessGuest=" + businessGuest +
                ", parking=" + parking +
                '}';
    }
}
