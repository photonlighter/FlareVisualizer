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

        Bundle extras = getIntent().getExtras();

        if (extras != null){
            data = extras.getStringArray("data");
            triggerType = extras.getString("triggerType");
        }

        TextView header = findViewById(R.id.triggerListHeader);
        header.setText(getString(R.string.trigger_list, triggerType));

        triggerList = findViewById(R.id.triggerList);

        DividerItemDecoration divDecoration = new DividerItemDecoration(triggerList.getContext(),
                layoutManager.getOrientation());
        triggerList.addItemDecoration(divDecoration);

        triggerList.hasFixedSize();

        layoutManager = new LinearLayoutManager(this);
        triggerList.setLayoutManager(layoutManager);

        adapter = new TriggerListAdapter(data);
        triggerList.setAdapter(adapter);
    }
}
