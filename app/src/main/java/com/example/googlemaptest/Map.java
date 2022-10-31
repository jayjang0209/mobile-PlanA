package com.example.googlemaptest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Objects;

public class Map extends Fragment {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public Map() {
        super(R.layout.activity_map);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map, container, false);

        // get the instance of the Firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        // get the reference to the JSON tree
        databaseReference = firebaseDatabase.getReference();

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        // Async map
        Objects.requireNonNull(mapFragment).getMapAsync(googleMap -> {

            databaseReference = firebaseDatabase.getReference();

            databaseReference.child("EvCharingStations").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int numStations = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        numStations += 1;
                        double lat = Double.parseDouble(String.valueOf(snapshot.child("Latitude").getValue()));
                        double lon = Double.parseDouble(String.valueOf(snapshot.child("Longitude").getValue()));
                        String address = String.valueOf(snapshot.child("Street Address").getValue());
                        LatLng station = new LatLng(lat, lon);

                        // Set the size of marker in map
                        int height = 100;
                        int width = 100;
                        BitmapDrawable bitmapdraw= (BitmapDrawable) getResources().getDrawable( R.drawable.ev_charing_station_icon);
                        Bitmap b = bitmapdraw.getBitmap();
                        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                        // Add markers to the googleMap
                        Objects.requireNonNull(googleMap.addMarker(new MarkerOptions()
                                        .position(station)
                                        .title(address)))
                                .setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
//                        Log.i("lat", String.valueOf(lat));
                    }
                    Log.i("Stations", String.valueOf(numStations));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            LatLng bcit = new LatLng(49.28357705407709, -123.11451451191841);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(bcit));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bcit, 12));
        });

        return view;
    }
}