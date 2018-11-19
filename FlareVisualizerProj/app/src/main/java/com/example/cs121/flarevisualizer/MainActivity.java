package com.example.cs121.flarevisualizer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.floor;

public class MainActivity extends HomeActivity {

    private LineChart lineChart;
    private LineChart[] mCharts = new LineChart[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_main, contentFrameLayout);

       /* lineChart = (LineChart) findViewById(R.id.chart); // LineChart is initialized from xml
        LineDataSet lineDataSet = new LineDataSet(dataValues(), "Data Set 1");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData lineData = new LineData(dataSets);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);

        lineDataSet.setFillAlpha(110);

        lineDataSet.setColor(Color.RED); // line color


        lineChart.setData(lineData);

        lineChart.invalidate();*/

        mCharts[0] = (LineChart) findViewById(R.id.chart1);
        mCharts[1] = (LineChart) findViewById(R.id.chart2);
        mCharts[2] = (LineChart) findViewById(R.id.chart3);
        mCharts[3] = (LineChart) findViewById(R.id.chart4);

        //initializes the data for the chart simulating a months worth of tracking (31 days, 1-10 pain scale)
        for (int i = 0; i < mCharts.length; i++) {
            LineData data = getData(31, 10);
            setupChart(mCharts[i], data, mColors[i]);
        }


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

    //random input of data for the y-axis is put in an ArrayList to graph
    private LineData getData(int count, int range){
        ArrayList<Entry> yVals = new ArrayList<>();

        for(int i = 0; i < count; i++){
            float val = (float)floor(Math.random()*range) + 1;
            yVals.add(new Entry(i, val));
        }
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

    //colors used for the backgrounds of each graph
    private int[] mColors = new int[]{
            Color.rgb(137, 230, 81), Color.rgb(240, 230, 30), Color.rgb(89, 199, 250), Color.rgb(250, 104, 119)
    };
    /*private ArrayList<Entry> dataValues(){

        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(0, 8));
        dataVals.add(new Entry(1, 9));
        dataVals.add(new Entry(2, 4));
        dataVals.add(new Entry(3, 5));
        dataVals.add(new Entry(4, 1));

        return dataVals;
    }*/

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
