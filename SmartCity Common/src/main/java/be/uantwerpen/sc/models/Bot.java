package be.uantwerpen.sc.models;

import be.uantwerpen.sc.models.links.Link;

import javax.persistence.*;

/**
 * This class defines basic attributes and methods for all child objects.
 *
 * @author Niels
 * @author Floris
 * Created by Niels on 24/03/2016.
 */
@Entity
@Inheritance
@DiscriminatorColumn(name = "vehicle_type")
@Table(name = "bot", schema = "", catalog = "core")
public class Bot {
    private Long id;
    private Long jobId; //deprecated, managed in MaaS
    private Long travelledDistance;
    private Integer percentageCompleted;
    private String state; //unused
    private Link linkId; //TODO: In documentation it seems this the  link the bot is currently on, find out if this is indeed the case

    @Id
    @Column(name = "rid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() { return id; }
    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "job_id")
    public Long getJobId() {
        return jobId;
    }
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    @Basic
    @Column(name = "percentage_completed")
    public Integer getPercentageCompleted() {
        return percentageCompleted;
    }
    public void setPercentageCompleted(Integer percentageCompleted) {
        this.percentageCompleted = percentageCompleted;
    }

    @Basic
    @Column(name = "state")
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Override of the inherited equals method. This method will compare if the given parameter is the same as this object itself.
     * It compares all attributes of the Bot object.
     *
     * @param o The bot object against which is to be checked.
     * @return true if the object is the same as this, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bot that = (Bot) o;

        if (id != that.id) return false;
        if (jobId != null ? !jobId.equals(that.jobId) : that.jobId != null) return false;
        if (percentageCompleted != null ? !percentageCompleted.equals(that.percentageCompleted) : that.percentageCompleted != null)
            return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;

        return true;
    }

    /**
     * Makes a hash for this object based on id, jobId, percentageCompleted and state
     *
     * @return The hash
     */
    @Override
    public int hashCode() {
        int result = (int) (id % Integer.MAX_VALUE);

        result = 31 * result + (jobId != null ? jobId.hashCode() : 0);
        result = 31 * result + (percentageCompleted != null ? percentageCompleted.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);

        return result;
    }

    @OneToOne
    @JoinColumn(name = "link_id", referencedColumnName = "lid")
    public Link getLinkId() {
        return linkId;
    }

    public void setLinkId(Link linkId) {
        this.linkId = linkId;
    }

    /**
     * Overrides the inherited toString() method
     *
     * @return string of this vehicles' id, idStart, idEnd and percentageCompleted
     */
    @Override
    public String toString() {

        Long idStart = 0L;
        Long idStop = 0L;


        if (getLinkId() != null) {
            idStart = getLinkId().getStartPoint().getId();
        }

        if (getLinkId() != null) {
            idStop = getLinkId().getStopPoint().getId();
        }


        String str = "{" +
                "\"idVehicle\" :" + getId() +
                ", \"idStart\" : " + idStart +
                ", \"idEnd\" : " + idStop +
                ", \"percentage\" : " + getPercentageCompleted() +
                "}";

        return str;
    }
}
