package com.example.cs121.flarevisualizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    protected ActionBarDrawerToggle toggle;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            drawer.closeDrawers();

            // Go to home screen

        } else if (id == R.id.nav_diet) {
            // Go to diet activity
            // from Sammy's code in MainActivity
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra("triggerType", "Diet");
            startActivity(intent);
            drawer.closeDrawers();

            // test data from Sammy, may be deleted in the future
            /*String[] testData = new String[4];
            for (int i =0; i < 4; ++i){
                testData[i] = "a" + i;
            }*/
            //intent.putExtra("data", testData);

        } else if (id == R.id.nav_activity) {
            // Go to exercise activity
            // from Sammy's code in MainActivity
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra("triggerType", "Activity");
            startActivity(intent);
            drawer.closeDrawers();

        } else if (id == R.id.nav_misc_triggers) {
            // Go to miscellaneous triggers activity
            // from Sammy's code in MainActivity
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra("triggerType", "Miscellany");
            startActivity(intent);
            drawer.closeDrawers();

        } else if (id==R.id.nav_flares) {
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra("triggerType","Flares");
            startActivity(intent);
            drawer.closeDrawers();

        } else if (id == R.id.nav_flare_edit) {
            // Go to flare edit activity
            Intent intent = new Intent(this, EditActivity.class);
            startActivity(intent);
            drawer.closeDrawers();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }
}
