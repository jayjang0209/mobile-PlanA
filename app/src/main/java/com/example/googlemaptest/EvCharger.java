package com.example.googlemaptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;

public class EvCharger extends AppCompatActivity {

    public EvCharger() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev_charger);

        // Initialize fragments
        Fragment first = new Map();
        Fragment second = new EvChargerConnectorList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ctnFragment, first);
        fragmentTransaction.commit();


        Button btnFirst = findViewById(R.id.buttonFragmentMap);
        // only one method -> lambda

        btnFirst.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
            fragmentTransaction1.replace(R.id.ctnFragment, first);
            fragmentTransaction1.commit();
        });

        Button btnSecond = findViewById(R.id.buttonFragmentList);
        btnSecond.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
            fragmentTransaction2.replace(R.id.ctnFragment, second);
            fragmentTransaction2.commit();
        });

    }
}