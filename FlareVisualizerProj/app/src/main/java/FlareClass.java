import android.util.Log;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

class TriggerLists {

    //for each mapping, the String is the key and the trigger,
    //while the Integer is the accumulated frequency with respect
    //to flares. The Int is updated every time a flare is declared
    //done with the by adding the average pain number of the flare
    //to the existing Int. Each trigger starts with an int of 0
    public Map<String, Integer> ActivityList;
    public Map<String, Integer> DietList;
    public Map<String, Integer> MiscList;

    public TriggerLists() {
        //Default constructor to retrieve the class object
    }

    public boolean doesTriggerExist(String trigger, String list) {
        boolean exists = false;
        if (list.equals("activity")) {
            exists = ActivityList.containsKey(trigger);
        } else if (list.equals("diet")) {
            exists = DietList.containsKey(trigger);
        } else if (list.equals("misc")) {
            exists = MiscList.containsKey(trigger);
        } else {
            Log.d("data", "Error, list does not exist to add triggers to");
        }

        return exists;
    }

    public void addNewTrigger(String trigger, String list) {
        if (list.equals("activity")) {
            ActivityList.put(trigger, 0);
        } else if (list.equals("diet")) {
            DietList.put(trigger, 0);
        } else if (list.equals("misc")) {
            MiscList.put(trigger, 0);
        } else {
            Log.d("data", "Error, list does not exist to add triggers to");
        }
    }

    public int getTriggerNum(String trigger, String list) {
        int trig = -1;
        if (list.equals("activity")){
            if (ActivityList.containsKey(trigger))
                trig = ActivityList.get(trigger);
            else
                Log.d("data", "Error, trigger does not exist");
        } else if (list.equals("diet")) {
            if (DietList.containsKey(trigger))
                trig = DietList.get(trigger);
            else
                Log.d("data", "Error, trigger does not exist");
        } else if (list.equals("misc")) {
            if (MiscList.containsKey(trigger))
                trig = MiscList.get(trigger);
            else
                Log.d("data", "Error, trigger does not exist");
        } else {

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

    public FlareDatabaseAbstract() {
        //Default constructor to retrieve class object
    }

    public void addFlareDatabaseAbstract(Timestamp startDate, Timestamp endDate,
                                      int avg, int length){
        start = startDate;
        end = endDate;
        avg_pain = avg;
        flare_length = length;
    }

}

public class FlareClass {
    private List<Integer> pain_nums;
    private List<String> times;

    public FlareClass() {
        //Default constructor to retrive class object
    }

    public void AddFlare (Integer pain, Timestamp time) {
        //Timestamp toString defaults to YYYY-MM-DD HH:MM:SS.ff where
        //ff is nanoseconds.
        pain_nums.add(pain);
        times.add(time.toString());
    }
}
