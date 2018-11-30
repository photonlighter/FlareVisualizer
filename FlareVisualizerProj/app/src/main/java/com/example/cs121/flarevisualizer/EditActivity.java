package com.example.cs121.flarevisualizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/*
 *  To do: Add an update flare and end flare button. On updating, retrieve the database object, update
 *  it with new info, and push it back.
 *  Ending requires no new info. On ending a flare, the most recent flare will be retrieved and we'll
 *  calculate the average pain and get the length of the flare. Both of these numbers, along with the
 *  start and end times will be stored in a general flare object and will be pushed to a database
 *  reference of that object type.
 *
 */
public class EditActivity extends HomeActivity {

    private String[] months = {"M", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private String[] days = new String[32];
    private String[] years = new String[21];
    private String[] hours = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private String[] meridiem = {"A.M.", "P.M."};
    private String[] painRating = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    private Spinner monthSpinner;
    private Spinner daySpinner;
    private Spinner yearSpinner;
    private Spinner hourSpinner;
    private Spinner meridiemSpinner;
    private Spinner painRatingSpinner;

    private CheckBox endFlare;
    private boolean endFlareChecked;
    String temp;
    long avg;

    private LinearLayout theTriggerLayout;

    //database references
    private FirebaseDatabase mDatabase;
    private DatabaseReference entryReferenceFlare;
    private DatabaseReference entryReferenceGeneral;
    private DatabaseReference entryReferenceActivity;
    private DatabaseReference entryReferenceDiet;
    private DatabaseReference entryReferenceMisc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_edit, contentFrameLayout);


        //set up database reference
        mDatabase = FirebaseDatabase.getInstance();
        entryReferenceFlare = mDatabase.getReference().child("Flares");
        entryReferenceGeneral = mDatabase.getReference().child("Abstract");
        entryReferenceActivity = mDatabase.getReference().child("Activity");
        entryReferenceDiet = mDatabase.getReference().child("Diet");
        entryReferenceMisc = mDatabase.getReference().child("Misc");

        monthSpinner = findViewById(R.id.monthSpinner);
        daySpinner = findViewById(R.id.daySpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        hourSpinner = findViewById(R.id.hourSpinner);
        meridiemSpinner = findViewById(R.id.meridiemSpinner);
        painRatingSpinner = findViewById(R.id.painRatingSpinner);

        endFlare = findViewById(R.id.endFlare);

        theTriggerLayout = findViewById(R.id.editTriggerLayout);

        populateDaysAndYears();
        setSpinners();
    }

    private void populateDaysAndYears() {
        days[0] = "D";
        for (int index = 1; index <= 31; ++index){
            days[index] = "" + index;
        }

        years[0] = "Y";
        for (int index = 1; index <= 20; ++index){
            years[index] = "" + (2019 - index);
        }
    }

    private void setSpinners() {
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, months);
        monthSpinner.setAdapter(monthAdapter);

        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, days);
        daySpinner.setAdapter(dayAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, years);
        yearSpinner.setAdapter(yearAdapter);

        ArrayAdapter<String> hourAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, hours);
        hourSpinner.setAdapter(hourAdapter);

        ArrayAdapter<String> meridiemAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, meridiem);
        meridiemSpinner.setAdapter(meridiemAdapter);

        ArrayAdapter<String> painRatingAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, painRating);
        painRatingSpinner.setAdapter(painRatingAdapter);
    }

    public void checkBoxClicked(View view) {
        endFlareChecked = true;
    }

    public void getOldInfo(View view) {
        // currently does nothing because of lack of database
        // month, day, year, hour, and meridiem spinners must be set to work
        // if not set, return error message/toaster
        // if set and old info exists, populate pain rating
        // if set and old info doesn't exist, do nothing
        if (monthSpinner.getSelectedItemPosition() <= 0 ||
                daySpinner.getSelectedItemPosition() <= 0 ||
                yearSpinner.getSelectedItemPosition() <= 0) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "All date and time fields must be filled out before submitting.",
                    Toast.LENGTH_SHORT);

            toast.show();
        }
    }

    public void submitNewInfo(View view) {
        SharedPreferences pref = getSharedPreferences("ProjectPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //NOTE: when using database, can find entries more efficiently via timestamp
        //NOTE: current contents are placeholders for when database is substituted in
        // get the index of the new entry and make a key for it

        if (monthSpinner.getSelectedItemPosition() <= 0 ||
                daySpinner.getSelectedItemPosition() <= 0 ||
                yearSpinner.getSelectedItemPosition() <= 0) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "All fields must be filled out before submitting.",
                    Toast.LENGTH_SHORT);

            toast.show();
            return;
        }
        String hour = hourSpinner.getSelectedItem().toString();
        int hourValue = Integer.valueOf(hour);
        if (((meridiemSpinner.getSelectedItem().toString() == "P.M.")
                && (hourValue != 12)) | ((meridiemSpinner.getSelectedItem().toString() == "A.M.")
                && (hourValue == 12))){
            hourValue = hourValue+12;
        }
        String timeStamp = yearSpinner.getSelectedItem().toString() + "-"
                + monthSpinner.getSelectedItem().toString() + "-"
                + daySpinner.getSelectedItem().toString() + " "
                + String.valueOf(hourValue) + ":"
                +"00:00.00";
        Timestamp time = Timestamp.valueOf(timeStamp);

        String pain = painRatingSpinner.getSelectedItem().toString();

        FlareClass flareC = new FlareClass();
        List<String> actTrig = new ArrayList<>();
        List<String> dietTrig = new ArrayList<>();
        List<String> miscTrig = new ArrayList<>();

        int index = pref.getInt("maxIndex", -1);
        String flare = "flare" + index;

        for (int i = 0; i < theTriggerLayout.getChildCount(); ++i) {
            View row = theTriggerLayout.getChildAt(i);
            Spinner rowSpinner = row.findViewById(R.id.triggerSpinner);
            EditText rowEdit = row.findViewById(R.id.triggerName);

            String rowType = rowSpinner.getSelectedItem().toString();
            String rowName = rowEdit.getText().toString().trim();

            if(!rowName.matches("")){
                //get trigger strings to add to the flare objects
                String triggerData = monthSpinner.getSelectedItem().toString() + "/" +
                        daySpinner.getSelectedItem() + "/" + yearSpinner.getSelectedItem() + ", " +
                        hourSpinner.getSelectedItem() + " " + meridiemSpinner.getSelectedItem()
                        + " - " + rowName;

                if (rowType.equals("Act.")){
                    actTrig.add(rowName);
                    //entryReferenceActivity.child(rowName).setValue(0);
                } else if (rowType.equals("Diet")) {
                    dietTrig.add(rowName);
                    //entryReferenceDiet.child(rowName).setValue(0);
                } else {
                    miscTrig.add(rowName);
                    //entryReferenceMisc.child(rowName).setValue(0);
                }

            }
        }

        //Set the data we've entered into the database
        flareC.UpdateFlare(pain, time, flare, actTrig, dietTrig, miscTrig);
        entryReferenceFlare.child(flare).setValue(flareC);

        editor.putInt("maxIndex", index);
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void updateInfo(View view) {
        SharedPreferences pref = getSharedPreferences("ProjectPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int index = pref.getInt("maxIndex", -1);
        final String flareN = "flare" + index;
        entryReferenceFlare.child(flareN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                FlareClass flareC = dataSnapshot.getValue(FlareClass.class);
                if (flareC == null) {
                    Toast.makeText(getApplicationContext(), "Error, no flare to update", Toast.LENGTH_LONG);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {

                    String hour = hourSpinner.getSelectedItem().toString();
                    int hourValue = Integer.valueOf(hour);
                    if (((meridiemSpinner.getSelectedItem().toString() == "P.M.")
                            && (hourValue != 12))  | ((meridiemSpinner.getSelectedItem().toString() == "A.M.")
                            && (hourValue == 12))) {
                        hourValue = hourValue+12;
                    }
                    String timeStamp = yearSpinner.getSelectedItem().toString() + "-"
                            + monthSpinner.getSelectedItem().toString() + "-"
                            + daySpinner.getSelectedItem().toString() + " "
                            + String.valueOf(hourValue) + ":"
                            +"00:00.00";

                    Timestamp time = Timestamp.valueOf(timeStamp);

                    String pain = painRatingSpinner.getSelectedItem().toString();
                    List<String> actTrig = new ArrayList<>();
                    List<String> dietTrig = new ArrayList<>();
                    List<String> miscTrig = new ArrayList<>();

                    for (int i = 0; i < theTriggerLayout.getChildCount(); ++i) {
                        View row = theTriggerLayout.getChildAt(i);
                        Spinner rowSpinner = row.findViewById(R.id.triggerSpinner);
                        EditText rowEdit = row.findViewById(R.id.triggerName);

                        String rowType = rowSpinner.getSelectedItem().toString();
                        String rowName = rowEdit.getText().toString().trim();

                        if (!rowName.matches("")) {
                            //get trigger strings to add to the flare objects

                            if (rowType.equals("Act.")) {
                                actTrig.add(rowName);
                                entryReferenceActivity.child(rowName).setValue(0);
                            } else if (rowType.equals("Diet")) {
                                dietTrig.add(rowName);
                                entryReferenceDiet.child(rowName).setValue(0);
                            } else {
                                miscTrig.add(rowName);
                                entryReferenceMisc.child(rowName).setValue(0);
                            }
                        }
                    }

                    flareC.UpdateFlare(pain, time, flareN, actTrig, dietTrig, miscTrig);

                    List<String> pains = flareC.getPain_Nums();
                    List<String> times = flareC.getTimes();
                    List<String> actTrigs = flareC.getActTriggers();
                    List<String> dietTrigs = flareC.getDietTriggers();
                    List<String> miscTrigs = flareC.getMiscTriggers();

                    DatabaseReference flareReference = dataSnapshot.getRef();
                    flareReference.setValue(flareC);


                    String dbID = flareC.getDbId();
                    int average = getPainAvg(pains);
                    List<String> startAndEnd = getStartAndEnd(times);
                    FlareDatabaseAbstract abstractFlare = new FlareDatabaseAbstract();
                    abstractFlare.setAvg_pain(average);
                    abstractFlare.setStartTime(startAndEnd.get(0));
                    abstractFlare.setEndTime(startAndEnd.get(1));
                    abstractFlare.setDbId(dbID);
                    entryReferenceGeneral.child(dbID).setValue(abstractFlare);

                    //Set the list frequencies
                    Iterator<String> trigIter = actTrigs.iterator();
                    while (trigIter.hasNext()) {
                        String temp = trigIter.next();
                    }

                    trigIter = dietTrigs.iterator();
                    while (trigIter.hasNext()) {
                        String temp = trigIter.next();
                    }

                    trigIter = miscTrigs.iterator();
                    while (trigIter.hasNext()) {
                        String temp = trigIter.next();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error updating flare", Toast.LENGTH_LONG);
            }

        });
        if (endFlareChecked) {
            editor.putInt("maxIndex", index + 1);
            endFlareChecked = false;
        }

        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

   /* public void updateTriggerLists(List<String> triggers, int average) {
        Iterator<String> trig = triggers.iterator();
        avg = average;
        ValueEventListener list1, list2, list3;
        list1 = getNewListener();
        list2 = getNewListener();
        list3 = getNewListener();
        while (trig.hasNext()) {
            temp = trig.next();
            Log.d("lists", temp);
            entryReferenceActivity.addListenerForSingleValueEvent(list1);
            entryReferenceDiet.addListenerForSingleValueEvent(list2);
            entryReferenceMisc.addListenerForSingleValueEvent(list3);
        }
    } */

   /*public void updateTriggerLists(DataSnapshot dataSnapshot) {
       FlareDatabaseAbstract flare = dataSnapshot.getValue(FlareDatabaseAbstract.class);
       List<String> trigs = flare.getFlare_triggers();

   }

    public ValueEventListener getNewListener() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(temp)) {
                    long oldAvg = (long) dataSnapshot.child(temp).getValue();
                    avg = avg+oldAvg;
                    dataSnapshot.getRef().child(temp).setValue(avg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error updating trigger freq", Toast.LENGTH_LONG);
            }
        };
        return listener;
    }*/

    public List<String> getStartAndEnd(List<String> times){
        String temp = "";
        List<String> result = new ArrayList<>();
        if (times != null) {
            temp = times.get(0);
            result.add(temp);
            temp = times.get(times.size()-1);
            result.add(temp);
        }
        return result;
    }

    public int getPainAvg (List<String> pain_nums) {
        int avg = 0;
        if (pain_nums != null) {
            int size = pain_nums.size();
            while (size > 0) {
                size = size - 1;
                int num = Integer.valueOf(pain_nums.get(size));
                avg = num + avg;
            }
            avg = avg/pain_nums.size();
        }
        return avg;
    }

    public void addTriggerField(View view) {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newField = null;
        if (inflater != null) {
            newField = inflater.inflate(R.layout.edit_trigger_row, null);
        }
        theTriggerLayout.addView(newField, theTriggerLayout.getChildCount());
    }

    public void deleteTriggerField(View view) {
        theTriggerLayout.removeView((View)view.getParent());
    }
}

