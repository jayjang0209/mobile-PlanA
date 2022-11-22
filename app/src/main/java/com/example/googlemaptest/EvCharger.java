package com.example.googlemaptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class EvCharger extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    public EvCharger() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev_charger);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        //disable Title of toolbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Initialize fragments
        Fragment first = new Map();
        Fragment second = new EvChargerConnectorList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ctnFragment, first);
        fragmentTransaction.commit();

//        Button btnFirst = findViewById(R.id.buttonFragmentMap);
//        btnFirst.setOnClickListener(view -> {
//            FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
//            fragmentTransaction1.replace(R.id.ctnFragment, first);
//            fragmentTransaction1.commit();
//        });
//
//        Button btnSecond = findViewById(R.id.buttonFragmentList);
//        btnSecond.setOnClickListener(view -> {
//            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
//            fragmentTransaction2.replace(R.id.ctnFragment, second);
//            fragmentTransaction2.commit();
//        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}