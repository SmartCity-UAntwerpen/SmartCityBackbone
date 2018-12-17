package be.uantwerpen.sc.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "link")
public class TransitLink {
    @Id
    int id;
    int weight;
    int stopid;
    int startid;

    public TransitLink(int id,  int startid, int stopid, int weight) {
        this.id = id;
        this.startid = startid;
        this.stopid = stopid;
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

    public int getStartId() {
        return startid;
    }

    public void setStartId(int startid) {
        this.startid = startid;
    }

    public int getStopId() {
        return stopid;
    }

    public void setStopId(int stopid) {
        this.startid = stopid;
    }
}
