package be.uantwerpen.sc.models;

import java.util.ArrayList;

public class Path {
    ArrayList<TransitLink> transitPath;
    int weight;

    void Path(){
        this.transitPath = new ArrayList<TransitLink>();
        this.weight = 0;
    }

    void Path(int weight, ArrayList<TransitLink> transitPath){
        this.transitPath = transitPath;
        this.weight = weight;
    }
    public void addWeight(int weight){
        this.weight+=weight;
    }

    public ArrayList<TransitLink> getTransitPath() {
        return transitPath;
    }

    public void setTransitPath(ArrayList<TransitLink> transitPath) {
        this.transitPath = transitPath;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    /*
    * Gets the combined weights of the TransitLinks in this path
    *
     */
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
            output += "[" + link.getStopId() + "-" + link.getStopId() + "],";
        }
        return output;
    }
}
