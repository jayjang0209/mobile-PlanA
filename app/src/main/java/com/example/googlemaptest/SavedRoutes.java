package com.example.googlemaptest;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class SavedRoutes extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<String> routes = new ArrayList<>();
    private ArrayList<String> routes_end;
    private boolean done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_routes);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        firebaseDatabase = FirebaseDatabase.getInstance();;
        firebaseDatabase.getReference().child("Trips").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        if (dsp.getKey().equals(userId)) {
                            for (DataSnapshot dsp2 : dsp.getChildren()) {
                               for (DataSnapshot dsp3 : dsp2.getChildren()) {
                                   Log.d(TAG, "onDataChange: " + dsp3.child("address").getValue(String.class));
                                   routes.add(dsp3.child("address").getValue(String.class));
                                   Log.d(TAG, "onDataChange: " + routes);
                               }
                            }
                        }
                    }
                    start();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled", error.toException());
            }});
        if (routes.size() == 0) {
            Toast.makeText(this, "No saved routes", Toast.LENGTH_LONG).show();
        }

    }

    private void start() {
        Bundle bundle = new Bundle();
        Log.d(TAG, "start: " + routes);
        bundle.putStringArrayList("routes", routes);

        FragmentRoutes fragmentRoutes = new FragmentRoutes();
        fragmentRoutes.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.linear_layout, fragmentRoutes).commit();
    }

}
