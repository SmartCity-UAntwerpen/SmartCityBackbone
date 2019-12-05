package be.uantwerpen.sc.models.map;

import javax.persistence.*;

@Entity
@Table(name = "Map")
public class Map {
    @Id
    @GeneratedValue( strategy= GenerationType.AUTO )
    int id;
    int offsetX;
    int offsetY;
    @ManyToOne
    @JoinColumn(name="access")
    MapAccessType access;

    public Map() {
    }

    public Map(int offsetX, int offsetY, MapAccessType access) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.access = access;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public MapAccessType getAccess() {
        return access;
    }

    public void setAccess(MapAccessType access) {
        this.access = access;
    }
}
