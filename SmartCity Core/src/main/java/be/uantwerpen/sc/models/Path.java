package be.uantwerpen.sc.models;

import java.util.ArrayList;

public class Path {
    ArrayList<TransitLink> transitPath;
    JobList jobList;
    float weight;

    public Path(){
        this.transitPath = new ArrayList<TransitLink>();
        this.weight = 0;
        this.jobList = new JobList();
    }

    public Path(float weight, ArrayList<TransitLink> transitPath){
        this.transitPath = transitPath;
        this.weight = weight;
        this.jobList = new JobList();
    }
    public void addWeight(float weight){
        this.weight+=weight;
    }

    public ArrayList<TransitLink> getTransitPath() {
        return transitPath;
    }

    public void setTransitPath(ArrayList<TransitLink> transitPath) {
        this.transitPath = transitPath;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    /*
    * Gets the combined weights of the TransitLinks in this path
    *
     */
    //TODO weights still in INT in database
    public int getTotalTransitWeight(){
        int transitWeight = 0;
        for(TransitLink transitLink : transitPath){
            transitWeight += transitLink.getWeight();
        }
        return transitWeight;
    }

    @Override
    public String toString() {
        String output = "";
        for(TransitLink link : transitPath){
            output += "[" + link.getStartId() + "-" + link.getStopId() + "],";
        }
        return output;
    }

    public JobList getJobList() {
        return jobList;
    }

    public void setJobList(JobList jobList) {
        this.jobList = jobList;
    }

    public void addJob(Job job){
        jobList.addJob(job);
    }

    public void addJob(long startid, long stopid, int mapid){
        Job job = new Job(startid, stopid, mapid);
        this.addJob(job);
    }

}
