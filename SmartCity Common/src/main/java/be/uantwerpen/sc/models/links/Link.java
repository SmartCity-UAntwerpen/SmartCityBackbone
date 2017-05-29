package be.uantwerpen.sc.models.links;

import be.uantwerpen.sc.models.points.Point;

import javax.persistence.*;

/**
 * Created by Niels on 24/03/2016.
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "link", schema = "", catalog = "smartcitydb")
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



    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Link that = (Link) o;

        if(id != that.id) return false;

        return true;
    }

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

    @Override
    public String toString() {
        return "LinkEntity{" +
                "id=" + id +
                ", startPoint=" + startPoint.getId() +
                ", stopPoint=" + stopPoint.getId() +
                ", acces=" + access +
                ", weight=" + weight +
                '}';
    }

    public String toStringBasic() {
        return "{" +
                "\"id\" : " + id +
                ", \"startPoint\" : " + startPoint.getId() +
                ", \"stopPoint\" : " + stopPoint.getId() +
                ", \"acces\" : \"" + access +
                "\", \"weight\" : " + weight +
                "}";
    }
}
