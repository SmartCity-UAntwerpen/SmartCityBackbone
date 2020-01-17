package be.uantwerpen.sc.models.mapbuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mblinklock")
public class LinkLock {
    @Id
    int id;

    public LinkLock(int id) {
        this.id = id;
    }

    public LinkLock() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
