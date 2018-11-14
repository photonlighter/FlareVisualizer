package com.example.cs121.flarevisualizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListActivity extends HomeActivity {

    private RecyclerView triggerList;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;
    private String[] data;
    private String triggerType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout container = findViewById(R.id.content_frame);
        View contentView = getLayoutInflater().inflate(R.layout.activity_list, null, false);
        container.addView(contentView, 0);

        // get intent from main activity
        Bundle extras = getIntent().getExtras();

        String rowType = "Act.";
        // fetch extras from the intent
        if (extras != null){
            // type of trigger data that is being displayed; used for header
            triggerType = extras.getString("triggerType");

            assert triggerType != null;
            if (triggerType.equals("Diet")){
                rowType = triggerType;
            } else if (triggerType.equals("Miscellany")){
                rowType = "Misc";
            }
        }

        // get trigger data that will be displayed in list; should be pre-formatted

        SharedPreferences pref = getSharedPreferences("ProjectPref", MODE_PRIVATE);
        int maxIndex = 0;
        if (rowType.equals("Act.")){
            maxIndex = pref.getInt("actIndex", -1);
        } else if (rowType.equals("Diet")){
            maxIndex = pref.getInt("dietIndex", -1);
        } else {
            maxIndex = pref.getInt("miscIndex", -1);
        }

        data = new String[maxIndex + 1];

        // populate array with all entries stored in file
        for (int currIndex = 0; currIndex <= maxIndex; ++currIndex) {
            String row = rowType + currIndex;
            data[currIndex] = pref.getString(row, null);
        }


                // set header according to the trigger type
        TextView header = findViewById(R.id.triggerListHeader);
        header.setText(getString(R.string.trigger_list, triggerType));

        // sets up the list of triggers
        triggerList = findViewById(R.id.triggerList);

        triggerList.hasFixedSize();

        layoutManager = new LinearLayoutManager(this);
        triggerList.setLayoutManager(layoutManager);

        DividerItemDecoration divDecoration = new DividerItemDecoration(triggerList.getContext(),
                layoutManager.getOrientation());
        triggerList.addItemDecoration(divDecoration);

        adapter = new TriggerListAdapter(data);
        triggerList.setAdapter(adapter);
    }
}
