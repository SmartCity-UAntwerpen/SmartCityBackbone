package be.uantwerpen.sc.models;

import javax.persistence.Entity;

@Entity
public class Point {
    private int id;
    private int mapId;

    public Point(int id, int mapid) {
        this.id = id;
        this.mapId = mapid;
    }

    public int getId() {
        return id;
    }

    public int getMapId() {
        return mapId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
}
