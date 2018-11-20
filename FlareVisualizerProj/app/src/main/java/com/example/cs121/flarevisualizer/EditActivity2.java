package com.example.cs121.flarevisualizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EditActivity2 extends HomeActivity {

    private String[] painRating = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

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
        getLayoutInflater().inflate(R.layout.activity_edit2, contentFrameLayout);

        //set up database reference
        mDatabase = FirebaseDatabase.getInstance();
        entryReferenceFlare = mDatabase.getReference().child("Flares");
        entryReferenceGeneral = mDatabase.getReference().child("Abstract");
        entryReferenceActivity = mDatabase.getReference().child("Activity");
        entryReferenceDiet = mDatabase.getReference().child("Diet");
        entryReferenceMisc = mDatabase.getReference().child("Misc");

        painRatingSpinner = findViewById(R.id.painRatingSpinner);
        ArrayAdapter<String> painRatingAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, painRating);
        painRatingSpinner.setAdapter(painRatingAdapter);

        theTriggerLayout = findViewById(R.id.editTriggerLayout);

        setSpinners();
    }


    private void setSpinners() {
        ArrayAdapter<String> painRatingAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, painRating);
        painRatingSpinner.setAdapter(painRatingAdapter);
    }


    /*public void getOldInfo(View view) {
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
    }*/

    public void submitNewInfo(View view) {
        SharedPreferences pref = getSharedPreferences("ProjectPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //instead of using the spinners (to be removed, sorry!), get the
        //timestamp of the current time
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String pain = painRatingSpinner.getSelectedItem().toString();
        FlareClass flareC = new FlareClass();
        List<String> trig = new ArrayList<>();

        int index = pref.getInt("maxIndex", -1) + 1;
        String flare = "flare" + index;

        for (int i = 0; i < theTriggerLayout.getChildCount(); ++i) {
            View row = theTriggerLayout.getChildAt(i);
            Spinner rowSpinner = row.findViewById(R.id.triggerSpinner);
            EditText rowEdit = row.findViewById(R.id.triggerName);

            String rowType = rowSpinner.getSelectedItem().toString();
            String rowName = rowEdit.getText().toString().trim();

            if(!rowName.matches("")){
                //get trigger strings to add to the flare objects
                trig.add(rowName);

                if (rowType.equals("Act.")){
                    entryReferenceActivity.child(rowName).setValue(0);
                } else if (rowType.equals("Diet")) {
                    entryReferenceDiet.child(rowName).setValue(0);
                } else {
                    entryReferenceMisc.child(rowName).setValue(0);
                }
            }
        }

        //Set the data we've entered into the database
        flareC.UpdateFlare(pain, time, flare, trig);
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
                Timestamp time = new Timestamp(System.currentTimeMillis());
                String pain = painRatingSpinner.getSelectedItem().toString();
                List<String> trig = new ArrayList<>();

                for (int i = 0; i < theTriggerLayout.getChildCount(); ++i) {
                    View row = theTriggerLayout.getChildAt(i);
                    Spinner rowSpinner = row.findViewById(R.id.triggerSpinner);
                    EditText rowEdit = row.findViewById(R.id.triggerName);

                    String rowType = rowSpinner.getSelectedItem().toString();
                    String rowName = rowEdit.getText().toString().trim();

                    if(!rowName.matches("")){
                        //get trigger strings to add to the flare objects
                        trig.add(rowName);

                        if (rowType.equals("Act.")){
                            entryReferenceActivity.child(rowName).setValue(0);
                        } else if (rowType.equals("Diet")) {
                            entryReferenceDiet.child(rowName).setValue(0);
                        } else {
                            entryReferenceMisc.child(rowName).setValue(0);
                        }
                    }
                }

                flareC.UpdateFlare(pain, time, flareN, trig);

                List <String> pains = flareC.getPain_Nums();
                List<String> times = flareC.getTimes();
                List<String> trigs = flareC.getTriggers();

                dataSnapshot.getRef().child("pain_Nums").setValue(pains);
                dataSnapshot.getRef().child("times").setValue(times);
                dataSnapshot.getRef().child("triggers").setValue(trigs);
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

