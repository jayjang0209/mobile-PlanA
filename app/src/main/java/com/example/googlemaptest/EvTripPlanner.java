package com.example.googlemaptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class EvTripPlanner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev_trip_planner);

        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.ev_trip_planner_fragment_container, new MapEv()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.ev_map_menu_map:
                        selectedFragment = new MapEv();
                        break;
                    case R.id.ev_map_menu_plan:
                        selectedFragment = new Map();
                        break;
                    case R.id.ev_map_menu_ev:
                        selectedFragment = new EvChargerConnectorList();
                        break;
                    case R.id.ev_map_menu_share:
                        Log.i("share", "share button clicked");
                        break;
                    default:
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.ev_trip_planner_fragment_container, selectedFragment).commit();
                }
                return false;
            }
        });

    }
}