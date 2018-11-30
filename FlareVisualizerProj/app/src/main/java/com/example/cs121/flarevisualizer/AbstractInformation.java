package com.example.cs121.flarevisualizer;

public class AbstractInformation {

    private String avg_pain;
    private String dbId;
    private String endTime;
    private String flare_length;
    private String startTime;

    public AbstractInformation() {

    }

    public String getAvg_pain() {
        return avg_pain;
    }

    public void setAvg_pain(String avg_pain) {
        this.avg_pain = avg_pain;
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFlare_length() {
        return flare_length;
    }

    public void setFlare_length(String flare_length) {
        this.flare_length = flare_length;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
