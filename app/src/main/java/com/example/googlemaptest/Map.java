package com.example.googlemaptest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng bcit = new LatLng(49.28357705407709, -123.11451451191841);
        googleMap.addMarker(new MarkerOptions()
                .position(bcit)
                .title("We are here!!"))
                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.cat_icon));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(bcit));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bcit, 12));
    }
}