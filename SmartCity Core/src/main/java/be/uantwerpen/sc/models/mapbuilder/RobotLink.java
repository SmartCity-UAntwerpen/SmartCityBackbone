package be.uantwerpen.sc.models.mapbuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mbrobotlink")
public class RobotLink {
    @Id
    int id;
    int angle;
    String destinationheading;
    String destinationnode;
    int distance;
    boolean islocal;
    int lockid;
    boolean loopback;
    String startheading;
    String startnode;
    String status;

    public RobotLink() {
    }

    public RobotLink(int id, int angle, String destinationheading, String destinationnode, int distance, boolean islocal, int lockid, boolean loopback, String startheading, String startnode, String status) {
        this.id = id;
        this.angle = angle;
        this.destinationheading = destinationheading;
        this.destinationnode = destinationnode;
        this.distance = distance;
        this.islocal = islocal;
        this.lockid = lockid;
        this.loopback = loopback;
        this.startheading = startheading;
        this.startnode = startnode;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public String getDestinationheading() {
        return destinationheading;
    }

    public void setDestinationheading(String destinationheading) {
        this.destinationheading = destinationheading;
    }

    public String getDestinationnode() {
        return destinationnode;
    }

    public void setDestinationnode(String destinationnode) {
        this.destinationnode = destinationnode;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isIslocal() {
        return islocal;
    }

    public void setIslocal(boolean islocal) {
        this.islocal = islocal;
    }

    public int getLockid() {
        return lockid;
    }

    public void setLockid(int lockid) {
        this.lockid = lockid;
    }

    public boolean isLoopback() {
        return loopback;
    }

    public void setLoopback(boolean loopback) {
        this.loopback = loopback;
    }

    public String getStartheading() {
        return startheading;
    }

    public void setStartheading(String startheading) {
        this.startheading = startheading;
    }

    public String getStartnode() {
        return startnode;
    }

    public void setStartnode(String startnode) {
        this.startnode = startnode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
