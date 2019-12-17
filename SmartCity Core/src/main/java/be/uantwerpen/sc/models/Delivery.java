package be.uantwerpen.sc.models;

import be.uantwerpen.sc.models.jobs.Job;
import be.uantwerpen.sc.models.jobs.JobList;

import javax.persistence.*;
import java.util.List;

/**
 * Created by NV 2018
 * The delivery is linked to a specific user.
 * It contains all the information of the delivery that is required to perform the Astart algorithm.
 * PointA is the start point of the delivery, while pointB is the end point of the delivery.
 * Its ID is retrieved from the MongoDB database.
 *
 */
@Entity
@Table(name = "deliveries")
public class Delivery extends MyAbstractPersistable<Long> {


    @OneToMany(cascade= CascadeType.ALL)
    @JoinColumn(name="idDelivery", referencedColumnName="ID")
    private List<JobList> jobLists;

    private int OrderID;
    private int pointA;
    private int pointB;
    private int mapA;
    private int mapB;
    private int passengers;
    private String type;
    private String date;
    private boolean complete = false;

    public Delivery() {
    }

    public Delivery(int orderID, int pointA, int pointB, int mapA, int mapB, int passengers, String type, String date) {
        OrderID = orderID;
        this.pointA = pointA;
        this.pointB = pointB;
        this.mapA = mapA;
        this.mapB = mapB;
        this.passengers = passengers;
        this.type = type;
        this.date = date;
    }

    public List<JobList> getJobLists() {
        return jobLists;
    }

    public int getPointA() {
        return pointA;
    }

    public int getPointB() {
        return pointB;
    }

    public int getMapA() {
        return mapA;
    }

    public int getMapB() {
        return mapB;
    }

    public int getPassengers() {
        return passengers;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public boolean isComplete() {
        return complete;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
