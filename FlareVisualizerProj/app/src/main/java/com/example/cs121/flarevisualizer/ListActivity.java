package com.example.cs121.flarevisualizer;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
