package be.uantwerpen.sc.models.mapbuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mbdronelink")
public class DroneLink {
    @Id
    int id;
    String lstart;
    String lend;
    String name;

    public DroneLink() {
    }

    public DroneLink(int id, String lstart, String lend, String name) {
        this.id = id;
        this.lstart = lstart;
        this.lend = lend;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLstart() {
        return lstart;
    }

    public void setLstart(String lstart) {
        this.lstart = lstart;
    }

    public String getLend() {
        return lend;
    }

    public void setLend(String lend) {
        this.lend = lend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
