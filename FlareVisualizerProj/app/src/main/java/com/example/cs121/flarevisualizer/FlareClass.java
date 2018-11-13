package com.example.cs121.flarevisualizer;

import android.util.Log;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class TriggerLists {

    //for each mapping, the String is the key and the trigger,
    //while the Integer is the accumulated frequency with respect
    //to flares. The Int is updated every time a flare is declared
    //done with the by adding the average pain number of the flare
    //to the existing Int. Each trigger starts with an int of 0
    private Map<String, Integer> ActivityList;
    private Map<String, Integer> DietList;
    private Map<String, Integer> MiscList;

    public TriggerLists() {
        //Default constructor to retrieve the class object
    }

    public boolean doesTriggerExist(String trigger, String list) {
        boolean exists = false;
        switch (list) {
            case "activity":
                exists = ActivityList.containsKey(trigger);
            case "diet":
                exists = DietList.containsKey(trigger);
            case "misc":
                exists = MiscList.containsKey(trigger);
            default: Log.d("data", "list does not exist");
        }

        return exists;
    }

    public void addNewTrigger(String trigger, String list) {
        switch (list) {
            case "activity":
                ActivityList.put(trigger, 0);
            case "diet":
                DietList.put(trigger, 0);
            case "misc":
             MiscList.put(trigger, 0);
            default: Log.d("data", "list does not exist");
        }
    }

    public Map<String, Integer> getTriggerList (String list) {
        switch (list) {
            case "activity":
                return ActivityList;
            case "diet":
                return DietList;
            case "misc":
                return MiscList;
            default:
                Log.d("data", "list does not exist");
                return null;
        }
    }
    public int getTriggerNum(String trigger, String list) {
        int trig = -1;
        switch (list) {
            case "activity":
                if (ActivityList.containsKey(trigger))
                    trig = ActivityList.get(trigger);
                else
                    Log.d("data", "Error, trigger does not exist");
            case "diet":
                if (DietList.containsKey(trigger))
                    trig = DietList.get(trigger);
                else
                    Log.d("data", "Error, trigger does not exist");
            case "misc":
                if (MiscList.containsKey(trigger))
                    trig = MiscList.get(trigger);
                else
                    Log.d("data", "Error, trigger does not exist");
            default:
                Log.d("data", "Error, list does not exist");
        }
        return trig;
    }

    public void setTriggerNum(String trigger, int num, String list) {
        if (list.equals("activity")) {
            ActivityList.put(trigger, num);
        } else if (list.equals("diet")) {
            DietList.put(trigger, num);
        } else if (list.equals("misc")) {
            MiscList.put(trigger, num);
        } else {
            Log.d("data", "Error, list does not exist");
        }
    }
}

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
    private List<Integer> pain_nums;
    private List<String> times;
    //triggers is a map containing "trigger","list type" for current flare
    private Map<String, String> triggers;
    private String dbId;

    public FlareClass() {
        //Default constructor to retrieve class object
    }

    public void AddFlare (int pain, Timestamp time/*, Map<String, String> triggers*/, String dbID) {
        //Timestamp toString defaults to YYYY-MM-DD HH:MM:SS.ff where
        //ff is nanoseconds.
        pain_nums.add(pain);
        times.add(time.toString());
        dbId = dbID;
        //this.triggers = triggers;
    }

    //get flare from database using ID and update it with the new numbers
    public FlareClass UpdateFlare (FlareClass flare, int pain, Timestamp time){
        flare.pain_nums.add(pain);
        flare.times.add(time.toString());
        return flare;
    }

    public FlareClass UpdateFlare (FlareClass flare, int pain, Timestamp time, Map<String,String> triggers){
        flare.pain_nums.add(pain);
        flare.times.add(time.toString());
        //use an iterator to add any new triggers to the existing triggers map
        Iterator<Map.Entry<String, String>> itr = triggers.entrySet().iterator();
        while(itr.hasNext())
        {
            Map.Entry<String, String> entry = itr.next();
            this.triggers.put(entry.getKey(), entry.getValue());
        }
        return flare;
    }

    public List<Integer> getPain_Nums () {
        return pain_nums;
    }

    public List<String> getTimes () {
        return times;
    }

    public void setDbId (String dbId) {
        this.dbId = dbId;
    }
    public String getDbId () {
        return dbId;
    }
}
