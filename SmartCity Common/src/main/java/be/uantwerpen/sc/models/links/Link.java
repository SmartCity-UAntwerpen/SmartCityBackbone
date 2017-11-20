package be.uantwerpen.sc.models.links;

import be.uantwerpen.sc.models.points.Point;

import javax.persistence.*;

/**
 * Defines the link object, a link is a connection between two tiles.
 * <p>A link is defined by an id, a startPoint, a stopPoint, the length, the weight and a string defining access </p>
 * <p>Start- and stopPoint are </p>
 *
 * @author Niels
 * @author Floris
 * Created by Niels on 24/03/2016.
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "link", schema = "", catalog = "core")
public class Link
{
    private Long id;
    private Point startPoint;
    private Point stopPoint;
    private Long length;
    private int weight;
    private String access;

    @Id
    @Column(name = "lid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }


    /**
     * Override of the inherited equals method. This method will compare if the given parameter is the same as this object itself.
     * It compares the id attribute of the Link object.
     *
     * @param o The bot object against which is to be checked.
     * @return true if the object is the same as this, false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Link that = (Link) o;

        if(id != that.id) return false;

        return true;
    }

    /**
     * Makes a hash for this object based on id
     *
     * @return The hash
     */
    @Override
    public int hashCode()
    {
        int result = (int)(id % Integer.MAX_VALUE);

        return result;
    }

    @OneToOne
    @JoinColumn(name = "start_point", referencedColumnName = "pid")
    public Point getStartPoint()
    {
        return startPoint;
    }

    public void setStartPoint(Point startPoint)
    {
        this.startPoint = startPoint;
    }

    @OneToOne
    @JoinColumn(name = "stop_point", referencedColumnName = "pid")
    public Point getStopPoint()
    {
        return stopPoint;
    }

    public void setStopPoint(Point stopPoint)
    {
        this.stopPoint = stopPoint;
    }

    @Basic
    @Column(name = "weight")
    public int getWeight()
    {
        return weight;
    }

    public void setWeight(int weight)
    {
        this.weight = weight;
    }

    @Basic
    @Column(name = "length")
    public Long getLength()
    {
        return length;
    }

    public void setLength(Long length)
    {
        this.length = length;
    }


    @Basic
    @Column(name = "access")
    public String getAccess(){return access; }

    public void setAccess(String acces)
    {
        this.access = acces;
    }

    /**
     * Overrides the inherited toString() method
     *
     * @return string of this link's id, startPoint, stopPoint, access and weight
     */
    @Override
    public String toString() {
        return "{" +
                "\"id\" :" + id +
                ", \"startPoint\" : " + startPoint.getId() +
                ", \"stopPoint\" : " + stopPoint.getId() +
                ", \"access\" : \" : " + access +
                "\", \"weight\" :" + weight +
                '}';
    }


    //TODO: figure out if this piece of code is needed, it is needed in SmartCity Core -> models.map -> CustomMap. Why this instead of toString()?
    //call method when avoiding toString method of the subclasses
    public String toStringTop() {
        return "{" +
                "\"id\" : " + id +
                ", \"startPoint\" : " + startPoint.toStringTop() +
                ", \"stopPoint\" : " + stopPoint.toStringTop() +
                ", \"vehicle\" : \"" + access +
                "\", \"weight\" : " + weight +
                "}";
    }
}
