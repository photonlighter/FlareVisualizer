package com.example.cs121.flarevisualizer;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends HomeActivity {

    private ListView triggerList;
    private ListAdapter adapter;
    //private String[] data;
    private String triggerType = "";

    private DatabaseReference mDatabase;
    private DatabaseReference flareDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout container = findViewById(R.id.content_frame);
        View contentView = getLayoutInflater().inflate(R.layout.activity_list, null, false);
        container.addView(contentView, 0);

        // get intent from main activity
        Bundle extras = getIntent().getExtras();

        //String rowType = "Act.";

        // sets up the list of triggers
        triggerList = (ListView) findViewById(R.id.triggerList);

        //triggerList.hasFixedSize();

        //layoutManager = new LinearLayoutManager(this);
        //triggerList.setLayoutManager(layoutManager);

        //DividerItemDecoration divDecoration = new DividerItemDecoration(triggerList.getContext(),
        //        layoutManager.getOrientation());
        //triggerList.addItemDecoration(divDecoration);

        boolean flareDisplay = false;
        // fetch extras from the intent
        if (extras != null) {
            // type of trigger data that is being displayed; used for header
            triggerType = extras.getString("triggerType");
            assert triggerType != null;

            // get trigger data that will be displayed in list; should be pre-formatted
            switch (triggerType) {
                case "Diet":
                    mDatabase = FirebaseDatabase.getInstance().getReference("Diet");
                    break;
                case "Activity":
                    mDatabase = FirebaseDatabase.getInstance().getReference("Activity");
                    break;
                case "Miscellany":
                    mDatabase = FirebaseDatabase.getInstance().getReference("Misc");
                    break;
                case "Flares":
                    mDatabase = FirebaseDatabase.getInstance().getReference("Flares");
                    flareDatabase = FirebaseDatabase.getInstance().getReference("Abstract");
                    triggerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String temp = (String) adapterView.getItemAtPosition(i);
                            Intent intent = new Intent(ListActivity.this, ListActivity.class);
                            intent.putExtra("triggerType", temp);
                            startActivity(intent);
                        }
                    });
                    triggerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                            final String temp = (String) adapterView.getItemAtPosition(i);
                            AlertDialog.Builder alert = (new AlertDialog.Builder(ListActivity.this));
                            alert.setTitle("Delete Flare:");
                            alert.setMessage("Are you sure you want to delete?");
                            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (mDatabase.child(temp).getKey() != null) {
                                        mDatabase.child(temp).removeValue();
                                        if (flareDatabase.child(temp).getKey() != null) {
                                            flareDatabase.child(temp).removeValue();
                                        }
                                        Toast.makeText(getApplicationContext(), "Flare removed", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Flare could not be removed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                                    dialogInterface.dismiss();
                                }
                            });

                            alert.show();
                            return true;
                        }
                    });
                    break;
                default:
                    //If we're here, then the triggerType in intent is a specific flare, not a list
                        flareDatabase = FirebaseDatabase.getInstance().getReference("Flares");
                        mDatabase = flareDatabase.child(triggerType);
                        flareDisplay = true;
                        break;
            }
        }

        // set header according to the trigger type
        TextView header = findViewById(R.id.triggerListHeader);
        header.setText(getString(R.string.trigger_list, triggerType));

        //Example from https://www.youtube.com/watch?v=2duc77R4Hqw
        //Retrieve Data from Firebase DB
        if (!flareDisplay) {
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    showData(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    showFlareData(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    // Obtain snapshot and send list to view
    private void showData(DataSnapshot dataSnapshot) {
        List<String> list = new ArrayList<>();
        String name;
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            name = ds.getKey();
            list.add(name);
        }
        // create adapter for list view to show Trigger List
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                list);
        triggerList.setAdapter(adapter);
    }

    private void showFlareData (DataSnapshot dataSnapshot) {
        List<String> list = new ArrayList<>();
        String name;
        List<String> pain = new ArrayList<>();
        List<String> times = new ArrayList<>();
        List<String> triggers = new ArrayList<>();
        DataSnapshot data;
        int size = 0;
        int pos = 0;

        data = dataSnapshot.child("pain_Nums");
        for (DataSnapshot ds : data.getChildren()) {
            name = (String) ds.getValue();
            pain.add(name);
        }
        data = dataSnapshot.child("times");
        for (DataSnapshot ds : data.getChildren()) {
            name = (String) ds.getValue();
            times.add(name);
        }
        data = dataSnapshot.child("triggers");
        if (data != null) {
            for (DataSnapshot ds : data.getChildren()) {
                name = (String) ds.getValue();
                triggers.add(name);
            }
        }
        list.add(dataSnapshot.getKey());
        list.add("PAIN REPORTS");
        list = makePainAndTimeDisplay(list, pain, times);
        list.add("TRIGGERS");
        list = makeTriggerDisplay(list, triggers);
        // create adapter for list view to show Trigger List
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                list);
        triggerList.setAdapter(adapter);
    }

    private List<String> makePainAndTimeDisplay (List<String> list, List<String> pain,
                                          List<String> time) {
        String times = "";
        String painNums = "";
        int pSize = pain.size();
        int tSize = time.size();
        while (!pain.isEmpty() && !time.isEmpty()) {
            pSize = pSize-1;
            tSize=tSize-1;
            times = time.get(tSize);
            painNums = pain.get(pSize);
            String tempTime = "Time: " + times;
            String tempPain = "     Pain: " + painNums;
            list.add(tempTime);
            list.add(tempPain);
            time.remove(tSize);
            pain.remove(pSize);
        }
        return list;
    }
    private List<String> makeTriggerDisplay (List<String> list, List<String> child) {
        if (!child.isEmpty()) {
            int size = child.size() - 1;
            while (!child.isEmpty()) {
                String temp = child.get(size);
                list.add(temp);
                child.remove(temp);
                size = size-1;
            }
        }
        return list;
    }
}
