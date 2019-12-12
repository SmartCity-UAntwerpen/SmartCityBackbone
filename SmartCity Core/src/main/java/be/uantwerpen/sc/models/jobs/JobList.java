package be.uantwerpen.sc.models.jobs;

import be.uantwerpen.sc.models.MyAbstractPersistable;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * NV 2018
 */
@Entity
public class JobList extends MyAbstractPersistable<Long> {

    @OneToMany(cascade= CascadeType.ALL)
    @JoinColumn(name="ID_ORDER", referencedColumnName="ID")
    private List<Job> jobs;

    @Column(name = "idDelivery", unique = false, nullable = false)
    private String idDelivery;

    public void addJob(Job job)
    {
        jobs.add(job);
    }

    public JobList() {
        this.jobs = new ArrayList<Job>();
        this.idDelivery = "-1";
    }

    public JobList(List<Job> jobs) {
        this.jobs = jobs;
    }

    public boolean isEmpty() {
        if (jobs.isEmpty()){
            return true;
        }
        else {
            return false;
        }
    }

    public int size() {
        return jobs.size();
    }
    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public String getIdDelivery() {
        return idDelivery;
    }

    public void setIdDelivery(String idDelivery) {
        this.idDelivery = idDelivery;
    }

}
