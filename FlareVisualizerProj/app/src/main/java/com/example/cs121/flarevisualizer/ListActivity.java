package com.example.cs121.flarevisualizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class ListActivity extends AppCompatActivity {

    private RecyclerView triggerList;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;
    private String[] data;
    private String triggerType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // get intent from main activity
        Bundle extras = getIntent().getExtras();

        // fetch extras from the intent
        if (extras != null){
            // trigger data that will be displayed in list; should be pre-formatted
            data = extras.getStringArray("data");
            // type of trigger data that is being displayed; used for header
            triggerType = extras.getString("triggerType");
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
