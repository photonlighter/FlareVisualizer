package com.example.cs121.flarevisualizer;
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
                            String temp = (String) adapterView.getItemAtPosition(i);
                            if (mDatabase.child(temp).getKey() != null) {
                                mDatabase.child(temp).removeValue();
                                if (flareDatabase.child(temp).getKey() != null) {
                                    flareDatabase.child(temp).removeValue();
                                }
                                Toast.makeText(getApplicationContext(), "Flare removed", Toast.LENGTH_LONG).show();
                                return true;
                            } else {
                                Toast.makeText(getApplicationContext(), "Flare could not be removed", Toast.LENGTH_LONG).show();
                                return false;
                            }
                        }
                    });
                    break;
                default:
                    //If we're here, then the triggerType in intent is a specific flare, not a list
                        flareDatabase = FirebaseDatabase.getInstance().getReference("Flares");
                        mDatabase = flareDatabase.child(triggerType);
                        break;
            }
        }

        // set header according to the trigger type
        TextView header = findViewById(R.id.triggerListHeader);
        header.setText(getString(R.string.trigger_list, triggerType));

        //Example from https://www.youtube.com/watch?v=2duc77R4Hqw
        //Retrieve Data from Firebase DB
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

}
