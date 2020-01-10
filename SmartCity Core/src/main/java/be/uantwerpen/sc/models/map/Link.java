package be.uantwerpen.sc.models.map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Link")
public class Link {

    @Id
    int id;
    int weight;
    int pointA;
    int pointB;



    public Link(){}

    public Link(int id,  int pointA, int pointB, int weight) {
        this.id = id;
        this.pointA = pointA;
        this.pointB = pointB;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getPointA() {
        return pointA;
    }

    public void setPointA(int pointA) {
        this.pointA = pointA;
    }

    public int getPointB() {
        return pointB;
    }

    public void setPointB(int pointB) {
        this.pointB = pointB;
    }
}
