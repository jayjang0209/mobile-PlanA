package com.example.googlemaptest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EvTripPlanner extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    // CURRENT USER ID
    String userId;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev_trip_planner);

        // get the instance of the Firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        // get the reference to the JSON tree
        databaseReference = firebaseDatabase.getReference();

//        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.ev_trip_planner_fragment_container, new MapEv()).commit();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        // Get current user
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        setHeaderTitle();

        setSupportActionBar(toolbar);
        //disable Title of toolbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment selectedFragment = null;
//                switch (item.getItemId()) {
//                    case R.id.ev_map_menu_map:
//                        selectedFragment = new MapEv();
//                        break;
//                    case R.id.ev_map_menu_plan:
//                        selectedFragment = new Map();
//                        break;
//                    case R.id.ev_map_menu_ev:
//                        selectedFragment = new EvChargerConnectorList();
//                        break;
//                    case R.id.ev_map_menu_share:
//                        Log.i("share", "share button clicked");
//                        break;
//                    default:
//                }
//
//                if (selectedFragment != null) {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.ev_trip_planner_fragment_container, selectedFragment).commit();
//                }
//                return false;
//            }
//        });

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.routes:
                intent = new Intent(EvTripPlanner.this, SavedRoutes.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            case R.id.cities:
                break;
            case R.id.budget:
                break;
            case R.id.signOut:
                intent = new Intent(EvTripPlanner.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
        }
        return false;
    }

    private void setHeaderTitle() {
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null && userId.equals(snapshot.getKey())) {
                        String name = user.getName();
                        String userFirstName = name.split(" ")[0];
                        // Set current user's name on the header title
                        View headerView = navigationView.getHeaderView(0);
                        TextView navUsername = headerView.findViewById(R.id.header_title);
                        navUsername.setText(String.format("Hi %s 👋", userFirstName));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}