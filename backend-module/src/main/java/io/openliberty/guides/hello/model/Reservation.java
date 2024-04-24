package io.openliberty.guides.hello.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Reservation")
public class Reservation {

    @Id
    @Column(name = "ReservationID")
    private int reservationId;

    @Column(name = "Start")
    private Date start;

    @Column(name = "End")
    private Date end;

    @Column(name = "State")
    private String state;

    @Column(name = "Cost")
    private float cost;

    @Column(name = "CommingTime")
    private Date commingTime;

    @Column(name = "LeavingTime")
    private Date leavingTime;

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

    public Date getCommingTime() {
        return commingTime;
    }

    public void setCommingTime(Date commingTime) {
        this.commingTime = commingTime;
    }

    public Date getLeavingTime() {
        return leavingTime;
    }

    public void setLeavingTime(Date leavingTime) {
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
