package be.uantwerpen.sc.models;

import javax.persistence.*;

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
    private String username;
    private String firstname;
    private String lastname;
    private String pointA;
    private String pointB;
    private int mapA;
    private int mapB;
    private int passengers;
    private String type;
    private String date;
    private boolean complete = false;
    private int backboneID;

    public Delivery() {
    }

    public Delivery(String username, String firstname, String lastname, String pointA, String pointB, int mapA, int mapB, int passengers, String type, String date, boolean complete, int backboneId) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.pointA = pointA;
        this.pointB = pointB;
        this.mapA = mapA;
        this.mapB = mapB;
        this.passengers = passengers;
        this.type = type;
        this.date = date;
        this.complete = complete;
        this.backboneID = backboneId;
    }
    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPointA() {
        return pointA;
    }

    public String getPointB() {
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

    public int getBackboneID() {
        return backboneID;
    }

}
