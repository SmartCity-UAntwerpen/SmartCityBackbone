package be.uantwerpen.sc.models.map;

import javax.persistence.*;

@Entity
@Table(name = "MapPoint")
public class MapPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    int pointId;


    @ManyToOne(targetEntity = Map.class)
    @JoinColumn(name = "mapId")
    Map map;
    int x;
    int y;
    @ManyToOne(targetEntity = MapPointType.class)
    @JoinColumn(name = "type")
    MapPointType mapPointType;

    public MapPoint() {
    }

    public MapPoint(int pointId, Map map, int x, int y, MapPointType mapPointType) {
        this.pointId = pointId;
        this.map = map;
        this.x = x;
        this.y = y;
        this.mapPointType = mapPointType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public MapPointType getMapPointType() {
        return mapPointType;
    }

    public void setMapPointType(MapPointType mapPointType) {
        this.mapPointType = mapPointType;
    }

    public int getMapId() {
        return map.id;
    }
}
