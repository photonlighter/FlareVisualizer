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
    private String start;
    private String end;
    private int avg_pain;
    private int flare_length;
    //this will be used to associate flares with the same id and get them easily
    private String dbId;

    public FlareDatabaseAbstract() {
        //Default constructor to retrieve class object
        start = "";
        end = "";
        avg_pain = 0;
        flare_length = 0;
        dbId = "";

    }

    public void addFlareDatabaseAbstract(Timestamp startDate, Timestamp endDate,
                                      int avg, int length, String dbID){
        start = startDate.toString();
        end = endDate.toString();
        avg_pain = avg;
        flare_length = length;
        dbId = dbID;
    }

    public void setStartTime (String start) {
        this.start = start;
    }
    public String getStartTime () {
        return this.start;
    }

    public void setEndTime (String end) {
        this.end = end;
    }
    public String getEndTime() {
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
        String temp;
        Timestamp temp2;
        if (times.size() > 0) {
            //get the most recent timestamp in times
            temp = times.get(times.size() - 1);
            temp2 = Timestamp.valueOf(temp);
            //if that timestamp is before the one being entered, enter it
            if (temp2.before(time))
                times.add(time.toString());
        }
        dbId = dbID;
        int size = triggers.size();
        temp = "";
        while(size > 0) {
            size = size - 1;
            temp = triggers.get(size);
            //check if the trigger list of this flare already has a trigger "added"
            if (!this.triggers.contains(temp)) {
                this.triggers.add(temp);
            }
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
