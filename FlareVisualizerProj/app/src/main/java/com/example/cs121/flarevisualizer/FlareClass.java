package com.example.cs121.flarevisualizer;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

class FlareDatabaseAbstract {
    private Timestamp start;
    private Timestamp end;
    private int avg_pain;
    private int flare_length;
    //this will be used to associate flares with the same id and get them easily
    private String dbId;

    public FlareDatabaseAbstract() {
        //Default constructor to retrieve class object
    }

    public void addFlareDatabaseAbstract(Timestamp startDate, Timestamp endDate,
                                      int avg, int length, String dbID){
        start = startDate;
        end = endDate;
        avg_pain = avg;
        flare_length = length;
        dbId = dbID;
    }

    public void setStartTime (Timestamp start) {
        this.start = start;
    }
    public Timestamp getStartTime () {
        return this.start;
    }

    public void setEndTime (Timestamp end) {
        this.end = end;
    }
    public Timestamp getEndTime() {
        return end;
    }

    public void setAvg_pain (int avg_pain) {
        this.avg_pain = avg_pain;
    }
    public int getAvg_pain () {
        return avg_pain;
    }

    public void setFlare_length (int flare_length) {
        this.flare_length = flare_length;
    }
    public int getFlare_length () {
        return flare_length;
    }

    public void setDbId (String dbId) {
        this.dbId = dbId;
    }
    public String getDbId () {
        return dbId;
    }

}

public class FlareClass {
    private List<String> pain_Nums;
    private List<String> times;
    private List<String> triggers;
    private String dbId;

    public FlareClass() {
        //Default constructor to retrieve class object
        pain_Nums = new ArrayList<>();
        times = new ArrayList<>();
        triggers = new ArrayList<>();
        dbId = "";
    }

    public void UpdateFlare (String painNum, Timestamp time, String dbID, List<String> triggers){
        pain_Nums.add(painNum);
        times.add(time.toString());
        dbId = dbID;
        int size = triggers.size();
        while(size > 0) {
            size = size - 1;
            this.triggers.add(triggers.get(size));
        }
    }

    public List<String> getPain_Nums () {
        if (pain_Nums != null)
            return pain_Nums;
        return null;
    }

    public List<String> getTimes () {
        if (times != null)
            return times;
        return null;
    }

    public List<String> getTriggers () {
        if (triggers != null)
            return triggers;
        return null;
    }

    public void setDbId (String dbId) {
        this.dbId = dbId;
    }
    public String getDbId () {
        return dbId;
    }
}
