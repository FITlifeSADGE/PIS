package io.openliberty.guides.hello.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Employee")
public class Employee {

    @Id
    @Column(name = "EmployeeID")
    private int employeeId;

    @Column(name = "Assignment")
    private String assignment;

    @Column(name = "Password")
    private String password;

    @Column(name = "WorkShift")
    private Date workShift;

    @OneToOne
    @JoinColumn(name = "EmployeeID")
    private Person person;

    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getWorkShift() {
        return workShift;
    }

    public void setWorkShift(Date workShift) {
        this.workShift = workShift;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", assignment='" + assignment + '\'' +
                ", password='" + password + '\'' +
                ", workShift=" + workShift +
                ", person=" + person +
                '}';
    }
}
