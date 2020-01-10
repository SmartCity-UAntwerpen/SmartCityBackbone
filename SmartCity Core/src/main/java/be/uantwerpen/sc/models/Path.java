package be.uantwerpen.sc.models;

import be.uantwerpen.sc.models.jobs.Job;
import be.uantwerpen.sc.models.jobs.JobList;
import be.uantwerpen.sc.models.map.Link;

import java.util.ArrayList;

public class Path {
    ArrayList<Link> transitPath;
    JobList jobList;
    float weight;

    public Path(){
        this.transitPath = new ArrayList<Link>();
        this.weight = 0;
        this.jobList = new JobList();
    }

    public Path(float weight, ArrayList<Link> transitPath){
        this.transitPath = transitPath;
        this.weight = weight;
        this.jobList = new JobList();
    }
    public void addWeight(float weight){
        this.weight+=weight;
    }

    public ArrayList<Link> getTransitPath() {
        return transitPath;
    }

    public void setTransitPath(ArrayList<Link> transitPath) {
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
        for(Link transitLink : transitPath){
            transitWeight += transitLink.getWeight();
        }
        return transitWeight;
    }

    @Override
    public String toString() {
        String output = "";
        for(Link link : transitPath){
            output += "[" + link.getPointA() + "-" + link.getPointB() + "],";
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

    public void addJob(long startid, long stopid, int mapid, float cost){
        Job job = new Job(startid, stopid, mapid, cost);
        this.addJob(job);
    }

}
