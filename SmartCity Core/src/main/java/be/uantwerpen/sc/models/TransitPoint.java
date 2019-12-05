package be.uantwerpen.sc.models;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transitpoint")
public class TransitPoint {
    @Id
    private int id;
    private int mapid;
    private int pid;

    public TransitPoint(){

    }
    public TransitPoint(int id, int mapid, int pid) {
        this.id = id;
        this.mapid = mapid;
        this.pid = pid;
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

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
