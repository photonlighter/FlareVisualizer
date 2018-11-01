package com.example.cs121.flarevisualizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private String[] months = {"M", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private String[] days = new String[32];
    private String[] years = new String[21];
    private String[] hours = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private String[] meridiem = {"A.M.", "P.M."};
    private String[] painRating = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    private Spinner monthSpinner;
    private Spinner daySpinner;
    private Spinner yearSpinner;
    private Spinner hourSpinner;
    private Spinner meridiemSpinner;
    private Spinner painRatingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        monthSpinner = findViewById(R.id.monthSpinner);
        daySpinner = findViewById(R.id.daySpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        hourSpinner = findViewById(R.id.hourSpinner);
        meridiemSpinner = findViewById(R.id.meridiemSpinner);
        painRatingSpinner = findViewById(R.id.painRatingSpinner);

        // make button invisible for now, as it does nothing
        // NOTE: make visible once it does something meaningful
        Button oldInfoBtn = findViewById(R.id.oldInfoBtn);
        oldInfoBtn.setVisibility(View.GONE);

        populateDaysAndYears();
        setSpinners();
    }

    private void populateDaysAndYears() {
        days[0] = "D";
        for (int index = 1; index <= 31; ++index){
            days[index] = "" + index;
        }

        years[0] = "Y";
        for (int index = 1; index < 21; ++index){
            years[index] = "" + (2019 - index);
        }
    }

    private void setSpinners() {
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, months);
        monthSpinner.setAdapter(monthAdapter);

        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, days);
        daySpinner.setAdapter(dayAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, years);
        yearSpinner.setAdapter(yearAdapter);

        ArrayAdapter<String> hourAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, hours);
        hourSpinner.setAdapter(hourAdapter);

        ArrayAdapter<String> meridiemAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, meridiem);
        meridiemSpinner.setAdapter(meridiemAdapter);

        ArrayAdapter<String> painRatingAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, painRating);
        painRatingSpinner.setAdapter(painRatingAdapter);
    }

    public void getOldInfo(View view) {
        // currently does nothing because of lack of database
        // month, day, year, hour, and meridiem spinners must be set to work
        // if not set, return error message/toaster
        // if set and old info exists, populate pain rating
        // if set and old info doesn't exist, do nothing
        if (monthSpinner.getSelectedItemPosition() <= 0 ||
                daySpinner.getSelectedItemPosition() <= 0 ||
                yearSpinner.getSelectedItemPosition() <= 0) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "All date and time fields must be filled out before submitting.",
                    Toast.LENGTH_SHORT);

            toast.show();
        }
    }

    public void submitNewInfo(View view) {
        SharedPreferences pref = getSharedPreferences("ProjectPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //NOTE: when using database, can find entries more efficiently via timestamp
        //NOTE: current contents are placeholders for when database is substituted in
        // get the index of the new entry and make a key for it

        if (monthSpinner.getSelectedItemPosition() <= 0 ||
                daySpinner.getSelectedItemPosition() <= 0 ||
                yearSpinner.getSelectedItemPosition() <= 0) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "All fields must be filled out before submitting.",
                    Toast.LENGTH_SHORT);

            toast.show();
            return;
        }

        int index = pref.getInt("maxIndex", -1) + 1;
        String flare = "flare" + index;
        String flareData = monthSpinner.getSelectedItem().toString() + "/" +
                daySpinner.getSelectedItem() + "/" + yearSpinner.getSelectedItem() + ", " +
                hourSpinner.getSelectedItem() + " " + meridiemSpinner.getSelectedItem() +
                " - pain rating " + painRatingSpinner.getSelectedItem();

        // put it in file
        editor.putString(flare, flareData);
        editor.putInt("maxIndex", index);

        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
