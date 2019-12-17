package be.uantwerpen.sc.models;

public class FailedJob {
    private int jobId;
    private int droneId;
    private String reason;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getDroneId() {
        return droneId;
    }

    public void setDroneId(int droneId) {
        this.droneId = droneId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
