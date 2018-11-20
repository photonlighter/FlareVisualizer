package com.example.cs121.flarevisualizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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

        String timeStamp = yearSpinner.getSelectedItem().toString() + "-"
                + monthSpinner.getSelectedItem().toString() + "-"
                + daySpinner.getSelectedItem().toString() + " "
                + hourSpinner.getSelectedItem().toString() + ":"
                +"00:00.00";
        Timestamp time = Timestamp.valueOf(timeStamp);

        String pain = painRatingSpinner.getSelectedItem().toString();

        FlareClass flareC = new FlareClass();
        List<String> trig = new ArrayList<>();

        int index = pref.getInt("maxIndex", -1) + 1;
        String flare = "flare" + index;

        int actIndex = pref.getInt("actIndex", -1);
        int dietIndex = pref.getInt("dietIndex", -1);
        int miscIndex = pref.getInt("miscIndex", -1);

        for (int i = 0; i < theTriggerLayout.getChildCount(); ++i) {
            View row = theTriggerLayout.getChildAt(i);
            Spinner rowSpinner = row.findViewById(R.id.triggerSpinner);
            EditText rowEdit = row.findViewById(R.id.triggerName);

            String rowType = rowSpinner.getSelectedItem().toString();
            String rowName = rowEdit.getText().toString().trim();

            trig.add(rowName);

            if(!rowName.matches("")){
                //get trigger strings to add to the flare objects
                trig.add(rowName);
                String triggerData = monthSpinner.getSelectedItem().toString() + "/" +
                        daySpinner.getSelectedItem() + "/" + yearSpinner.getSelectedItem() + ", " +
                        hourSpinner.getSelectedItem() + " " + meridiemSpinner.getSelectedItem()
                        + " - " + rowName;

                if (rowType.equals("Act.")){
                    entryReferenceActivity.child(rowName).setValue(0);
                    ++actIndex;
                    rowType += actIndex;
                } else if (rowType.equals("Diet")) {
                    entryReferenceDiet.child(rowName).setValue(0);
                    ++dietIndex;
                    rowType += dietIndex;
                } else {
                    entryReferenceMisc.child(rowName).setValue(0);
                    ++miscIndex;
                    rowType += miscIndex;
                }

                editor.putString(rowType, triggerData);
            }
        }

        //Set the data we've entered into the database
        flareC.UpdateFlare(pain, time, flare, trig);
        entryReferenceFlare.child(flare).setValue(flareC);

        editor.putInt("maxIndex", index);
        editor.putInt("actIndex", actIndex);
        editor.putInt("dietIndex", dietIndex);
        editor.putInt("miscIndex", miscIndex);

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

                    String timeStamp = yearSpinner.getSelectedItem().toString() + "-"
                            + monthSpinner.getSelectedItem().toString() + "-"
                            + daySpinner.getSelectedItem().toString() + " "
                            + hourSpinner.getSelectedItem().toString() + ":"
                            +"00:00.00";
                    Timestamp time = Timestamp.valueOf(timeStamp);

                    String pain = painRatingSpinner.getSelectedItem().toString();
                    List<String> trig = new ArrayList<>();

                    for (int i = 0; i < theTriggerLayout.getChildCount(); ++i) {
                        View row = theTriggerLayout.getChildAt(i);
                        Spinner rowSpinner = row.findViewById(R.id.triggerSpinner);
                        EditText rowEdit = row.findViewById(R.id.triggerName);

                        String rowType = rowSpinner.getSelectedItem().toString();
                        String rowName = rowEdit.getText().toString().trim();

                        if (!rowName.matches("")) {
                            //get trigger strings to add to the flare objects
                            trig.add(rowName);

                            if (rowType.equals("Act.")) {
                                entryReferenceActivity.child(rowName).setValue(0);
                            } else if (rowType.equals("Diet")) {
                                entryReferenceDiet.child(rowName).setValue(0);
                            } else {
                                entryReferenceMisc.child(rowName).setValue(0);
                            }
                        }
                    }

                    flareC.UpdateFlare(pain, time, flareN, trig);

                    List<String> pains = flareC.getPain_Nums();
                    List<String> times = flareC.getTimes();
                    List<String> trigs = flareC.getTriggers();

                    dataSnapshot.getRef().child("pain_Nums").setValue(pains);
                    dataSnapshot.getRef().child("times").setValue(times);
                    dataSnapshot.getRef().child("triggers").setValue(trigs);

                    if (endFlareChecked) {
                        //Add code to create the abstract database object from FlareClassAbstract
                        entryReferenceFlare.child(flareN).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                FlareClass endFlare = dataSnapshot.getValue(FlareClass.class);
                                //add methods to average pain, get the start and end times and length, and
                                //update the triggers in lists with new frequency values
                                List<String> pain_nums = endFlare.getPain_Nums();
                                List<String> times = endFlare.getTimes();
                                List<String> triggers = endFlare.getTriggers();
                                String dbID = endFlare.getDbId();
                                int average = getPainAvg(pain_nums);
                                int length = getFlareLength(times);
                                List<String> startAndEnd = getStartAndEnd(times);
                                FlareDatabaseAbstract abstractFlare = new FlareDatabaseAbstract();
                                abstractFlare.setAvg_pain(average);
                                abstractFlare.setStartTime(startAndEnd.get(0));
                                abstractFlare.setEndTime(startAndEnd.get(1));
                                abstractFlare.setDbId(dbID);
                                abstractFlare.setFlare_length(length);

                                entryReferenceGeneral.child(dbID).setValue(abstractFlare);

                                updateTriggerLists(triggers);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Error ending flare", Toast.LENGTH_LONG);
                            }
                        });
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

    public void updateTriggerLists(List<String> triggers) {
        //Add code to update frequency of triggers in lists
    }

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

    public int getFlareLength (List<String> times) {
        Timestamp s;
        Timestamp e;
        int hours = 0;
        long len = 0;
        if (times != null) {
            s = Timestamp.valueOf(times.get(1));
            e = Timestamp.valueOf(times.get(times.size()-1));
            len = e.getTime() - s.getTime();
            int seconds = (int) len / 1000;
            hours = seconds/3600;
        }
        return hours;
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

