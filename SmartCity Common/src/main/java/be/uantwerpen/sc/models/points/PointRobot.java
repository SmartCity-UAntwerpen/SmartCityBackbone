package be.uantwerpen.sc.models.points;

import be.uantwerpen.sc.models.points.Point;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by quent on 5/10/2017.
 */
@Entity
@Table(name="point_robot", schema = "", catalog = "smartcitydb")
public class PointRobot extends Point {

    private String rfid;
    private int pointLock;

    @Basic
    @Column(name = "rfid")
    public String getRfid()
    {
        return rfid;
    }

    public void setRfid(String rfid)
    {
        this.rfid = rfid;
    }

    @Basic
    @Column(name = "pointlock")
    public int getPointLock()
    {
        return pointLock;
    }

    public void setPointLock(int pointLock)
    {
        this.pointLock = pointLock;
    }

    @Override

    public String toString()

    {

        return "{" +

                "\"id\" : " + getId() +

                ", \"rfid\" :\"" + rfid + "\"" +

                ", \"pointLock\" : " + pointLock +

                '}';

    }
}
