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
@Table(name="link_robot", schema = "", catalog = "smartcitydb")
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
}
