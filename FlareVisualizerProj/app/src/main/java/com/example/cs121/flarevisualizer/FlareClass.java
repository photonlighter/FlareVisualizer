package com.example.cs121.flarevisualizer;

import android.util.Log;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

class TriggerLists {

    //for each mapping, the String is the key and the trigger,
    //while the Integer is the accumulated frequency with respect
    //to flares. The Int is updated every time a flare is declared
    //done with the by adding the average pain number of the flare
    //to the existing Int. Each trigger starts with an int of 0
    private List<Triggers> ActivityList;
    private List<Triggers> DietList;
    private List<Triggers> MiscList;

    public TriggerLists() {
        //Default constructor to retrieve the class object
        ActivityList = new ArrayList<>();
        DietList = new ArrayList<>();
        MiscList = new ArrayList<>();

    }

    public boolean doesTriggerExist(String trigger, String list) {
        boolean exists = false;
        Triggers temp = new Triggers();
        switch (list) {
            case "activity":
                ListIterator<Triggers> iteratorA = ActivityList.listIterator();
                while (iteratorA.hasNext()) {
                    temp = iteratorA.next();
                    if (temp.containsTrigger(trigger)) {
                        exists = true;
                    }
                }
            case "diet":
                ListIterator<Triggers> iteratorD = DietList.listIterator();
                while (iteratorD.hasNext()) {
                    temp = iteratorD.next();
                    if (temp.containsTrigger(trigger)) {
                        exists = true;
                    }
                }
            case "misc":
                ListIterator<Triggers> iteratorM = MiscList.listIterator();
                while (iteratorM.hasNext()) {
                    temp = iteratorM.next();
                    if (temp.containsTrigger(trigger)) {
                        exists = true;
                    }
                }
            default: Log.d("data", "list does not exist");
        }

        return exists;
    }

    public void addNewTrigger(String trigger, String list) {
        Triggers temp = new Triggers (trigger, 0);
        switch (list) {
            case "activity":
                ActivityList.add(temp);
            case "diet":
                DietList.add(temp);
            case "misc":
                MiscList.add(temp);
            default: Log.d("data", "list does not exist");
        }
    }

    public List<Triggers> getTriggerList (String list) {
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
        Triggers temp = new Triggers();
        switch (list) {
            case "activity":
                ListIterator<Triggers> iteratorA = ActivityList.listIterator();

                while (iteratorA.hasNext()) {
                    temp = iteratorA.next();
                    if (temp.containsTrigger(trigger)) {
                        trig = temp.getFreq();
                    }
                }
            case "diet":
                ListIterator<Triggers> iteratorD = DietList.listIterator();
                while (iteratorD.hasNext()) {
                    temp = iteratorD.next();
                    if (temp.containsTrigger(trigger)) {
                        trig = temp.getFreq();
                    }
                }
            case "misc":
                ListIterator<Triggers> iteratorM = MiscList.listIterator();
                while (iteratorM.hasNext()) {
                    temp = iteratorM.next();
                    if (temp.containsTrigger(trigger)) {
                        trig = temp.getFreq();
                    }
                }
            default:
                Log.d("data", "Error, list does not exist");
        }
        return trig;
    }

    public void setTriggerNum(String trigger, int num, String list) {
       Triggers temp = new Triggers();
       switch(list) {
           case "activity":
               ListIterator<Triggers> iteratorA = ActivityList.listIterator();
               while (iteratorA.hasNext()) {
                   temp = iteratorA.next();
                   if (temp.containsTrigger(trigger)) {
                       temp.setFreq(num);
                       iteratorA.set(temp);
                   }
               }
           case "Diet":
               ListIterator<Triggers> iteratorD = DietList.listIterator();
               while (iteratorD.hasNext()) {
                   temp = iteratorD.next();
                   if (temp.containsTrigger(trigger)) {
                       temp.setFreq(num);
                       iteratorD.set(temp);
                   }
               }
           case "Misc":
               ListIterator<Triggers> iteratorM = ActivityList.listIterator();
               while (iteratorM.hasNext()) {
                   temp = iteratorM.next();
                   if (temp.containsTrigger(trigger)) {
                       temp.setFreq(num);
                       iteratorM.set(temp);
                   }
               }
           default:
               Log.d("data", "Error, list does not exist");
       }
    }
}

class Triggers {
    private String trigger;
    private int freq;

    public Triggers () {
        //default constructor
        trigger = "";
        freq = 0;
    }

    public Triggers (String trigger, int freq) {
        this.trigger = trigger;
        this.freq = freq;
    }

    public void setFreq (int freq) {
        this.freq = freq;
    }

    public int getFreq () {
        return freq;
    }

    public boolean containsTrigger (String trigger) {
        if (this.trigger == trigger) { return true; }
        else { return false; }
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
    private List<String> triggers;
    private String dbId;

    public FlareClass() {
        //Default constructor to retrieve class object
        pain_nums = new ArrayList<>();
        times = new ArrayList<>();
        triggers = new ArrayList<>();
        dbId = "";
    }

    public void UpdateFlare(Integer pain, Timestamp time, String dbID, List<String> triggers) {
        //Timestamp toString defaults to YYYY-MM-DD HH:MM:SS.ff where
        //ff is nanoseconds.
        pain_nums.add(pain);
        times.add(time.toString());
        dbId = dbID;
        int size = triggers.size();
        while(size > 0) {
            size = size - 1;
            this.triggers.add(triggers.get(size));
        }
    }

    //get flare from database using ID and update it with the new numbers
    public FlareClass UpdateFlare (FlareClass flare, int pain, Timestamp time){
        flare.pain_nums.add(pain);
        flare.times.add(time.toString());
        return flare;
    }

    public FlareClass UpdateFlare (FlareClass flare, int pain, Timestamp time, List<String> triggers){
        flare.pain_nums.add(pain);
        flare.times.add(time.toString());
        int size = triggers.size();
        while(size > 0) {
            size = size - 1;
            this.triggers.add(triggers.get(size));
        }
        return flare;
    }

    public List<Integer> getPain_Nums () {
        return pain_nums;
    }

    public List<String> getTimes () {
        return times;
    }

    public List<String> getTriggers () {
        return triggers;
    }

    public void setDbId (String dbId) {
        this.dbId = dbId;
    }
    public String getDbId () {
        return dbId;
    }
}
