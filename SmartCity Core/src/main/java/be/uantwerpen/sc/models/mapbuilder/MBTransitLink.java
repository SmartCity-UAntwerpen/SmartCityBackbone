package be.uantwerpen.sc.models.mapbuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mbtransitlink")
public class MBTransitLink {
    @Id
    int id;
    String name;
    String tlstart;
    String tlend;

    public MBTransitLink(int id, String name, String tlstart, String tlend) {
        this.id = id;
        this.name = name;
        this.tlstart = tlstart;
        this.tlend = tlend;
    }

    public MBTransitLink() {
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

    public String getTlstart() {
        return tlstart;
    }

    public void setTlstart(String tlstart) {
        this.tlstart = tlstart;
    }

    public String getTlend() {
        return tlend;
    }

    public void setTlend(String tlend) {
        this.tlend = tlend;
    }
}
