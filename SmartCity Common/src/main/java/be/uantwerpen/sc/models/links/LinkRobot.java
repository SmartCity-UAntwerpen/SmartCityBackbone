package be.uantwerpen.sc.models.links;

import be.uantwerpen.sc.models.links.Link;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by quent on 5/10/2017.
 */
@Entity
@Table(name="link_robot", schema = "", catalog = "core")
public class LinkRobot extends Link {

    private String startDirection;
    private String stopDirection;


    @Basic
    @Column(name = "start_direction")
    public String getStartDirection()
    {
        return startDirection;
    }

    public void setStartDirection(String startDirection)
    {
        this.startDirection = startDirection;
    }

    @Basic
    @Column(name = "stop_direction")
    public String getStopDirection()
    {
        return stopDirection;
    }

    public void setStopDirection(String stopDirection)
    {
        this.stopDirection = stopDirection;
    }

    /**
     * Overrides the inherited toString() method
     *
     * @return string of this link's id, startPoint, stopPoint, access and weight
     */
    @Override
    public String toString() {

        return "{" +

                "\"id\" : " + getId() +

                ", \"length\" : " + getLength() +

                ", \"startPoint\" : " + getStartPoint().getId() +

                ", \"stopPoint\" : " + getStopPoint().getId() +

                ", \"startDirection\" : \"" + startDirection + "\"" +

                ", \"stopDirection\" : \"" + stopDirection + "\"" +

                ", \"weight\" :" + getWeight() +

                '}';

    }
}
