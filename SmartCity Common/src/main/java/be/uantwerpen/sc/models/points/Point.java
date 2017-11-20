package be.uantwerpen.sc.models.points;

import javax.persistence.*;

/**
 *
 * @author Niels
 * Created by Niels on 24/03/2016.
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "point", schema = "", catalog = "core")
public class Point
{
    private Long id;

    private String pointType; //TODO: figure out different types a point can be
    private String access; //TODO: what types of access
    private Long hub; //TODO: What is the hub
    private int xCoord;
    private int yCoord;


    @Id
    @Column(name = "pid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Basic
    @Column(name = "type")
    public String getPointType()
    {
        return pointType;
    }

    public void setPointType(String pointType)
    {
        this.pointType = pointType;
    }

    @Basic
    @Column(name = "hub")
    public Long getHub()
    {
        return hub;
    }

    public void setHub(Long hub)
    {
        this.hub = hub;
    }

    @Basic
    @Column(name = "access")
    public String getAccess()
    {
        return access;
    }

    public void setAccess(String access)
    {
        this.access = access;
    }

    @Basic
    @Column(name = "xcoord")
    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    @Basic
    @Column(name = "ycoord")
    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }


    /**
     * Overrides inherited equals method
     * <p>Commpares class, id and pointType</p>
     *
     * @param o The object o be checked against
     * @return
     */
    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Point that = (Point) o;

        if(id != that.id) return false;
        if(pointType != null ? !pointType.equals(that.pointType) : that.pointType != null) return false;

        return true;
    }

    /**
     * Makes a hash for this object based on id and pointType
     *
     * @return The hash
     */
    @Override
    public int hashCode()
    {
        int result = (int)(id % Integer.MAX_VALUE);

        result = 31 * result + (pointType != null ? pointType.hashCode() : 0);

        return result;
    }

    //TODO: Again toStringTop
    public String toStringTop()
    {
        return "{" +
                " \"id\" : " + id +
                ", \"x\" : " + xCoord +
                ", \"y\" : " + yCoord +
                ", \"type\" : \"" + access + //pointType refers to vehicle pointType in the Maas
                "\"}";
    }
}
