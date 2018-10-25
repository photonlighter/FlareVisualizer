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
    public void test(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        String[] testData = new String[4];
        for (int i =0; i < 4; ++i){
            testData[i] = "a" + i;
        }

        intent.putExtra("data", testData);
        intent.putExtra("triggerType", "hi");

        startActivity(intent);
    }
}
