package com.example.googlemaptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends Fragment {

    public Map() {
        super(R.layout.activity_map);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map, container, false);

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        // Async map
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
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
        });

        return view;
    }


//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        googleMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng bcit = new LatLng(49.28357705407709, -123.11451451191841);
//        googleMap.addMarker(new MarkerOptions()
//                .position(bcit)
//                .title("We are here!!"))
//                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.cat_icon));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(bcit));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bcit, 12));
//    }
}