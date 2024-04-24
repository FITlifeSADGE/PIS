package io.openliberty.guides.hello.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Service")
public class Service {

    @Id
    @Column(name = "ServiceID")
    private int serviceId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Cost")
    private float cost;

    @Column(name = "Availability")
    private String availability;

    @Column(name = "Description")
    private String description;

    // Getters and Setters
    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceId=" + serviceId +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", availability='" + availability + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
