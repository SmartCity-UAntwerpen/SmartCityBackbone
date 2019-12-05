package be.uantwerpen.sc.models.map;

import javax.persistence.*;

@Entity
@Table(name = "MapPointType")
public class MapPointType {
    @Id
    @GeneratedValue( strategy= GenerationType.AUTO )
    int id;
    String type;

    public MapPointType() {
    }

    public MapPointType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
