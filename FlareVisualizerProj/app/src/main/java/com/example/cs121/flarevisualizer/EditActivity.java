package com.example.cs121.flarevisualizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


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

        // get spinners
        monthSpinner = findViewById(R.id.monthSpinner);
        daySpinner = findViewById(R.id.daySpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        hourSpinner = findViewById(R.id.hourSpinner);
        meridiemSpinner = findViewById(R.id.meridiemSpinner);
        painRatingSpinner = findViewById(R.id.painRatingSpinner);

        theTriggerLayout = findViewById(R.id.editTriggerLayout);

        populateDaysAndYears();
        setSpinners();
    }

    // populates the day and year arrays
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

    // puts the correct data in the corresponding spinner
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

    // adds a new flare to the database
    public void submitNewInfo(View view) {
        SharedPreferences pref = getSharedPreferences("ProjectPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (monthSpinner.getSelectedItemPosition() <= 0 ||
                daySpinner.getSelectedItemPosition() <= 0 ||
                yearSpinner.getSelectedItemPosition() <= 0) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "All fields must be filled out before submitting.",
                    Toast.LENGTH_SHORT);

            toast.show();
            return;
        }
        //converts the hours into military time
        String hour = hourSpinner.getSelectedItem().toString();
        int hourValue = Integer.valueOf(hour);
        if (((meridiemSpinner.getSelectedItem().toString() == "P.M.")
                && (hourValue != 12)) | ((meridiemSpinner.getSelectedItem().toString() == "A.M.")
                && (hourValue == 12))){
            hourValue = hourValue+12;
        }

        // creates the timestamp
        String timeStamp = yearSpinner.getSelectedItem().toString() + "-";
        if (Integer.parseInt(monthSpinner.getSelectedItem().toString()) / 10 == 0){
            timeStamp += "0";
        }
        timeStamp += monthSpinner.getSelectedItem().toString() + "-";
        if (Integer.parseInt(daySpinner.getSelectedItem().toString()) / 10 == 0){
            timeStamp += "0";
        }
        timeStamp += daySpinner.getSelectedItem().toString() + " ";
        if (Integer.parseInt(daySpinner.getSelectedItem().toString()) / 10 == 0){
            timeStamp += "0";
        }
        if (hourValue / 10 == 0){
            timeStamp += "0";
        }
        timeStamp += String.valueOf(hourValue) + ":" +"00:00.00";

        Timestamp time = Timestamp.valueOf(timeStamp);

        String pain = painRatingSpinner.getSelectedItem().toString();

        FlareClass flareC = new FlareClass();
        List<String> actTrig = new ArrayList<>();
        List<String> dietTrig = new ArrayList<>();
        List<String> miscTrig = new ArrayList<>();

        int index = pref.getInt("maxIndex", -1) + 1;
        String flare = "flare" + index;

        // gets the suspected triggers
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
                    entryReferenceActivity.child(rowName).child(flare).setValue(0);
                    //updateTriggerFreq(entryReferenceActivity);
                } else if (rowType.equals("Diet")) {
                    dietTrig.add(rowName);
                    entryReferenceDiet.child(rowName).child(flare).setValue(0);
                    //updateTriggerFreq(entryReferenceDiet);
                } else {
                    miscTrig.add(rowName);
                    entryReferenceMisc.child(rowName).child(flare).setValue(0);
                    //updateTriggerFreq(entryReferenceMisc);
                }

            }
        }

        //Set the data we've entered into the database
        boolean check;
        check = flareC.updateFlare(pain, time, flare, actTrig, dietTrig, miscTrig);
        if (!check) {
            Toast.makeText(getApplicationContext(), "Error inserting pain report", Toast.LENGTH_LONG).show();
        }
        entryReferenceFlare.child(flare).setValue(flareC);

        editor.putInt("maxIndex", index);
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // adds onto the current flare
    public void updateInfo(View view) {

        SharedPreferences pref = getSharedPreferences("ProjectPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (monthSpinner.getSelectedItemPosition() <= 0 ||
                daySpinner.getSelectedItemPosition() <= 0 ||
                yearSpinner.getSelectedItemPosition() <= 0) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "All fields must be filled out before submitting.",
                    Toast.LENGTH_SHORT);

            toast.show();
            return;
        }

        //int index = pref.getInt("maxIndex", -1);
        //final String flareN = "flare" + index;
        entryReferenceFlare.orderByChild("dbId").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        FlareClass flareC = data.getValue(FlareClass.class);
                        if (flareC == null) {
                            Toast.makeText(getApplicationContext(), "Error, no flare to update", Toast.LENGTH_LONG);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            // turns the hours into military time
                            String flareN = flareC.getDbId();
                            String hour = hourSpinner.getSelectedItem().toString();
                            int hourValue = Integer.valueOf(hour);
                            if (((meridiemSpinner.getSelectedItem().toString() == "P.M.")
                                    && (hourValue != 12)) | ((meridiemSpinner.getSelectedItem().toString() == "A.M.")
                                    && (hourValue == 12))) {
                                hourValue = hourValue + 12;
                            }

                            //create the timestamp
                            String timeStamp = yearSpinner.getSelectedItem().toString() + "-";
                            if (Integer.parseInt(monthSpinner.getSelectedItem().toString()) / 10 == 0) {
                                timeStamp += "0";
                            }
                            timeStamp += monthSpinner.getSelectedItem().toString() + "-";
                            if (Integer.parseInt(daySpinner.getSelectedItem().toString()) / 10 == 0) {
                                timeStamp += "0";
                            }
                            timeStamp += daySpinner.getSelectedItem().toString() + " ";
                            if (Integer.parseInt(daySpinner.getSelectedItem().toString()) / 10 == 0) {
                                timeStamp += "0";
                            }
                            if (hourValue / 10 == 0) {
                                timeStamp += "0";
                            }
                            timeStamp += String.valueOf(hourValue) + ":" + "00:00.00";

                            Timestamp time = Timestamp.valueOf(timeStamp);

                            String pain = painRatingSpinner.getSelectedItem().toString();
                            List<String> actTrig = new ArrayList<>();
                            List<String> dietTrig = new ArrayList<>();
                            List<String> miscTrig = new ArrayList<>();

                            // gets the suspected triggers
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
                                    } else if (rowType.equals("Diet")) {
                                        dietTrig.add(rowName);
                                    } else {
                                        miscTrig.add(rowName);
                                    }
                                }
                            }

                            boolean check = true;
                            check = flareC.updateFlare(pain, time, flareN, actTrig, dietTrig, miscTrig);
                            if (!check) {
                                Toast.makeText(getApplicationContext(), "Error inserting pain report", Toast.LENGTH_LONG).show();
                            }

                            List<String> pains = flareC.getPain_Nums();
                            List<String> times = flareC.getTimes();
                            List<String> actTrigs = flareC.getActTriggers();
                            List<String> dietTrigs = flareC.getDietTriggers();
                            List<String> miscTrigs = flareC.getMiscTriggers();

                            DatabaseReference flareReference = data.getRef();
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
                                if (temp != null) {
                                    entryReferenceActivity.child(temp).child(dbID).setValue(average);
                                    updateTriggerFreq(entryReferenceActivity);
                                }
                            }

                            trigIter = dietTrigs.iterator();
                            while (trigIter.hasNext()) {
                                String temp = trigIter.next();
                                if (temp != null) {
                                    entryReferenceDiet.child(temp).child(dbID).setValue(average);
                                    updateTriggerFreq(entryReferenceDiet);
                                }
                            }

                            trigIter = miscTrigs.iterator();
                            while (trigIter.hasNext()) {
                                String temp = trigIter.next();
                                if (temp != null) {
                                    entryReferenceMisc.child(temp).child(dbID).setValue(average);
                                    updateTriggerFreq(entryReferenceMisc);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error updating flare", Toast.LENGTH_LONG);
            }

        });

        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // gets the first and last entry in the list
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

    // gets the average pain in the list
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

    public void updateTriggerFreq(DatabaseReference triggerRef) {
        triggerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int freq;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    freq = 0;
                    ds.getRef().child("freq").setValue(0);
                    for (DataSnapshot snap : ds.getChildren()) {
                        if (!snap.getKey().equals("freq")) {
                            int currFreq = snap.getValue(Integer.class);
                            freq = currFreq + freq;
                        }
                    }
                    ds.getRef().child("freq").setValue(freq);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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