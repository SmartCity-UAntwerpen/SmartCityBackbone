package be.uantwerpen.sc.models;

import javax.persistence.*;

/**
 * Created by Niels on 24/03/2016.
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "point", schema = "", catalog = "smartcitydb")
public class Point
{
    private Long id;

    private String type;
    private String acces;


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
    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Basic
    @Column(name = "acces")
    public String getAcces()
    {
        return type;
    }

    public void setAcces(String acces)
    {
        this.acces = acces;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Point that = (Point) o;

        if(id != that.id) return false;
        if(type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = (int)(id % Integer.MAX_VALUE);

        result = 31 * result + (type != null ? type.hashCode() : 0);

        return result;
    }

    @Override
    public String toString()
    {
        return "PointEntity{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
