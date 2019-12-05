package be.uantwerpen.sc.models.map;

import javax.persistence.*;

@Entity
@Table(name = "MapAccessType")
public class MapAccessType {
    @Id
    @GeneratedValue( strategy= GenerationType.AUTO )
    int id;
    String name;

    public MapAccessType() {
    }

    public MapAccessType(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
