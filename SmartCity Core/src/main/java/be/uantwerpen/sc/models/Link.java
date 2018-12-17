package be.uantwerpen.sc.models;

import javax.persistence.Entity;

@Entity
public class Link {
    int id;
    int weight;
    int stopid;
    int startid;

    public Link(int id,  int startid, int stopid, int weight) {
        this.id = id;
        this.startid = stopid;
        this.startid = stopid;
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
