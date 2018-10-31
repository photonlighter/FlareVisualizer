package com.example.cs121.flarevisualizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // used to debug ListActivity; kept for future reference, but can be removed as desired
    public void moveToList(View view) {
        Intent intent = new Intent(this, ListActivity.class);

        intent.putExtra("triggerType", "Activity");

        startActivity(intent);
    }

    // used to debug EditActivity; kept for future reference, but can be removed as desired
    public void moveToEdit(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }
}
