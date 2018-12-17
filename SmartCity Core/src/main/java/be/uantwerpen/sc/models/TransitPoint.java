package be.uantwerpen.sc.models;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "point")
public class TransitPoint {
    @Id
    private int id;
    private int mapid;

    public TransitPoint(){

    }
    public TransitPoint(int id, int mapid) {
        this.id = id;
        this.mapid = mapid;
    }

    public int getId() {
        return id;
    }

    public int getMapid() {
        return mapid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMapid(int mapId) {
        this.mapid = mapId;
    }
}
