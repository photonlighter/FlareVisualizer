package com.example.cs121.flarevisualizer;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends HomeActivity {

    private LineChart lineChart;
    private LineChart[] mCharts = new LineChart[2];

    private DatabaseReference flareDatabase;
    private DatabaseReference abstractDatabase;

    public ArrayList<Entry> yVals = new ArrayList<>();
    public ArrayList<Entry> yValsAbstract = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_main, contentFrameLayout);

        // Pain numbers for latest flare
        mCharts[0] = (LineChart) findViewById(R.id.chart1);

        // Average pain numbers from abstract
        mCharts[1] = (LineChart) findViewById(R.id.chart2);

        setupFlareData();

        setupAbstractData();
    }

    // setup for the current flare's graph
    private void setupFlareData() {
        SharedPreferences pref = getSharedPreferences("ProjectPref", MODE_PRIVATE);
        int index = pref.getInt("maxIndex", -1);
        String flare = "flare" + index;

        flareDatabase = FirebaseDatabase.getInstance().getReference("Flares");

        //Get most recent Flares record
        //Query recentRecord = flareDatabase.child("Flares").orderByKey().limitToLast(1);

        flareDatabase.orderByChild("dbId").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getFlareData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // setup for the averages graph
    private void setupAbstractData() {
        abstractDatabase = FirebaseDatabase.getInstance().getReference("Abstract");

        abstractDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getAbstractData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // gets data for current flare's graph
    private void getFlareData(DataSnapshot dataSnapshot) {
        Float xNumber;
        //DataSnapshot data = dataSnapshot.child("pain_Nums");
        if (dataSnapshot != null) {
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                FlareClass currFlare = data.getValue(FlareClass.class);
                if (currFlare == null) {
                    return;
                }
                List<String> painNums = currFlare.getPain_Nums();
                int counter = 0;
                Iterator<String> iter = painNums.iterator();
                while (iter.hasNext()) {
                    String temp = iter.next();
                    Log.d("graph: ", temp);
                    if (temp != null) {
                        xNumber = Float.parseFloat(temp);
                        yVals.add(new Entry(counter, xNumber));
                        counter++;
                    }
                }
            }
        }
        LineData flareData = setChartProperties(yVals);
        setupChart(mCharts[0], flareData, mColors[0]);
    }

    // gets data for the averages graph
    private void getAbstractData(DataSnapshot dataSnapshot) {
        Float avgPain = Float.valueOf(0);
        int counter = 0;
        DataSnapshot data;
        if (dataSnapshot != null) {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                data = ds.child("avg_pain");
                avgPain = data.getValue(Float.class);
                yValsAbstract.add(new Entry(counter, avgPain));
                counter++;
            }
        }
        LineData abstractData = setChartProperties(yValsAbstract);
        setupChart(mCharts[1], abstractData, mColors[1]);

    }

    // creates the general properties of a chart
    private LineData setChartProperties(ArrayList<Entry> yVals) {
        LineDataSet set1 = new LineDataSet(yVals, "Data Set");

        //graph formatting
        set1.setLineWidth(3f);
        set1.setCircleRadius(5f);
        set1.setCircleHoleRadius(2.5f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setHighLightColor(Color.WHITE);
        set1.setDrawValues(true);
        set1.setHighlightEnabled(true);
        set1.setDrawHighlightIndicators(true);


        set1.setDrawCircles(true);

        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);

        set1.setDrawFilled(true);
        set1.setFillColor(Color.CYAN);
        set1.setFillAlpha(80);

        //custom gradient to color the lower bound of the graph
        //Drawable drawable = ContextCompat.getDrawable(this, R.drawable.gradient1);
        //set1.setFillDrawable(drawable);

        LineData data = new LineData(set1);
        return data;
    }

    //formatting the chart
    private void setupChart(LineChart chart, LineData data, int color){
        ((LineDataSet) data.getDataSetByIndex(0)).setCircleHoleColor(color);

        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);

        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setHighlightPerDragEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.setMaxVisibleValueCount(10);
        chart.setKeepPositionOnRotation(true);

        chart.setBackgroundColor(color);
        chart.setViewPortOffsets(10, 0, 10, 0);

        Legend l = chart.getLegend();
        l.setEnabled(true);

        chart.setData(data);
    }


    //colors used for the backgrounds of each graph
    private int[] mColors = new int[]{
            Color.rgb(137, 230, 81),
            Color.rgb(240, 230, 30),
            Color.rgb(89, 199, 250),
            Color.rgb(250, 104, 119)
    };


}
