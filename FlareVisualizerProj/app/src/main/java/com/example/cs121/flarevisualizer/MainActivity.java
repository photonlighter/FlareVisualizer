package com.example.cs121.flarevisualizer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LineChart lineChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart = (LineChart) findViewById(R.id.chart); // LineChart is initialized from xml
        LineDataSet lineDataSet = new LineDataSet(dataValues(), "Data Set 1");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData lineData = new LineData(dataSets);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);


        lineChart.setData(lineData);

        lineChart.invalidate();

/*
        ArrayList<Entry> yValues = new ArrayList<>();

        yValues.add(new Entry(0, 20f));
        yValues.add(new Entry(1, 70f));
        yValues.add(new Entry(2, 30f));
        yValues.add(new Entry(3, 50f));
        yValues.add(new Entry(4, 50f));
        yValues.add(new Entry(5, 10f));
        yValues.add(new Entry(6, 60f));

        LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");

        set1.setFillAlpha(110);

        set1.setColor(Color.RED); // line color

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // adds line from yValues

        LineData data = new LineData(dataSets);

        lineChart.setData(data);
        lineChart.invalidate(); // refresh
*/
    }


    private ArrayList<Entry> dataValues(){

        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(0, 20));
        dataVals.add(new Entry(1, 50));
        dataVals.add(new Entry(2, 30));
        dataVals.add(new Entry(3, 10));
        dataVals.add(new Entry(4, 40));

        return dataVals;
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
