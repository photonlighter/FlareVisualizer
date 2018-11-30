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
    //this will be used to associate flares with the same id and get them easily
    private String dbId;

    public FlareDatabaseAbstract() {
        //Default constructor to retrieve class object
        start = "";
        end = "";
        avg_pain = 0;
        dbId = "";

    }

    public void addFlareDatabaseAbstract(Timestamp startDate, Timestamp endDate,
                                      int avg, String dbID){
        start = startDate.toString();
        end = endDate.toString();
        avg_pain = avg;
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
    private List<String> actTriggers;
    private List<String> dietTriggers;
    private List<String> miscTriggers;
    private String dbId;

    public FlareClass() {
        //Default constructor to retrieve class object
        pain_Nums = new ArrayList<>();
        times = new ArrayList<>();
        actTriggers = new ArrayList<>();
        dietTriggers = new ArrayList<>();
        miscTriggers = new ArrayList<>();
        dbId = "";
    }

    public void UpdateFlare (String painNum, Timestamp time, String dbID, List<String> actTriggers,
                             List<String> dietTriggers, List<String> miscTriggers){
        pain_Nums.add(painNum);
        times.add(time.toString());
        dbId = dbID;
        setTrigList(this.actTriggers, actTriggers);
        setTrigList(this.dietTriggers, dietTriggers);
        setTrigList(this.miscTriggers, miscTriggers);
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

    public List<String> getActTriggers () {
        if (actTriggers != null)
            return actTriggers;
        return null;
    }
    public List<String> getDietTriggers () {
        if (dietTriggers != null)
            return dietTriggers;
        return null;
    }
    public List<String> getMiscTriggers () {
        if (actTriggers != null)
            return miscTriggers;
        return null;
    }

    public void setDbId (String dbId) {
        this.dbId = dbId;
    }
    public String getDbId () {
        return dbId;
    }

    //private helper method to set each trigger list
    private void setTrigList(List<String> currTrigs, List<String> newTrigs) {
        String trig;
        Iterator<String> iter = newTrigs.iterator();
        while (iter.hasNext()) {
            trig = iter.next();
            if (!currTrigs.contains(trig)) {
                currTrigs.add(trig);
            }
        }

    }
}
