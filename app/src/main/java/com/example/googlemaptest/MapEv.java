package com.example.googlemaptest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapEv extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener
        ,GoogleMap.OnInfoWindowClickListener {

    public static final float DEFAULT_RANGE = 450000;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    ImageView imageViewSearch;
    EditText locationInput;
    GoogleMap googleMapRef;
    ArrayList<Circle> circles;
    Bitmap chargerMarkerIcon;
    Bitmap savedMarkerIcon;
    ArrayList<LatLng> savedMarkers;
    ArrayList<EvStation> savedEvStations;
    Polyline mapPolyline;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map_ev, container, false);

//        imageViewSearch = view.findViewById(R.id.locationSearch);
//        locationInput = view.findViewById(R.id.locationInput);


        // get the instance of the Firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        // get the reference to the JSON tree
        databaseReference = firebaseDatabase.getReference();

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapEv);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Setup maker icons
        BitmapDrawable bitmapdraw= (BitmapDrawable) getResources().getDrawable( R.drawable.ev_charing_station_icon);
        Bitmap b = bitmapdraw.getBitmap();
        chargerMarkerIcon = Bitmap.createScaledBitmap(b, 85, 85, false);
        BitmapDrawable bitmapdrawSaved = (BitmapDrawable) getResources().getDrawable( R.drawable.ev_saved_marker_icon);
        Bitmap bSaved = bitmapdrawSaved.getBitmap();
        savedMarkerIcon = Bitmap.createScaledBitmap(bSaved, 85, 85, false);


        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Initial map camera coordinate
        LatLng bcit = new LatLng(49.28357705407709, -123.11451451191841);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(bcit));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bcit, 12));

        googleMapRef = googleMap;
        circles = new ArrayList<>();
        savedMarkers = new ArrayList<>();
        savedEvStations = new ArrayList<>();
        mapPolyline = googleMap.addPolyline(new PolylineOptions());

        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);

//        // Set onclick listener to search place button
//        imageViewSearch.setOnClickListener(view1 -> {
//            String location = locationInput.getText().toString();
//            if (location != null && !location.equals("")) {
//                List<Address> addressList = null;
//                Geocoder geocoder = new Geocoder(getActivity());
//                try {
//                    addressList = geocoder.getFromLocationName(location, 1);
//                    Log.i("address", addressList.toString());
//                    if(addressList.size() > 0) {
//                        LatLng latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
//                        googleMap.addMarker(new MarkerOptions().position(latLng).title(location));
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Toast.makeText(getActivity(), "Please enter a location", Toast.LENGTH_SHORT).show();
//            }
//        });

        databaseReference = firebaseDatabase.getReference();

        // Access to the Realtime database
        databaseReference.child("EvCharingStations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numStations = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    numStations += 1;

                    // Get station information
                    double lat = Double.parseDouble(String.valueOf(snapshot.child("Latitude").getValue()));
                    double lon = Double.parseDouble(String.valueOf(snapshot.child("Longitude").getValue()));
                    String address = String.valueOf(snapshot.child("Street Address").getValue());
                    String connectorType = String.valueOf(snapshot.child("EV Connector Types").getValue());
                    LatLng station = new LatLng(lat, lon);
                    int chargerNumber;
                    try {
                        chargerNumber = Integer.parseInt(String.valueOf(snapshot.child("EV Level2 EVSE Num").getValue()));
                    } catch (Exception e) {
                        chargerNumber = Integer.parseInt(String.valueOf(snapshot.child("EV DC Fast Count").getValue()));
                    }

                    // Add markers to the googleMap
                    Objects.requireNonNull(googleMap.addMarker(new MarkerOptions()
                                    .position(station)
                                    .title(address)
                                    .snippet("Connector: " + connectorType + '\n' + "Chargers: " + String.valueOf(chargerNumber))))
                            .setIcon(BitmapDescriptorFactory.fromBitmap(chargerMarkerIcon));
                }
                Log.i("Stations", String.valueOf(numStations));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
        Log.i("Circle center", String.valueOf(marker.getPosition()));

        // remove previous circles
        for (Circle circle: circles) {
            circle.remove();
        }
        circles.clear();

        // draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(marker.getPosition());
        circleOptions.radius(DEFAULT_RANGE);
        circleOptions.strokeColor(0x20f24e1e);
        circleOptions.fillColor(0x40f24e1e);
        Circle rangeCircle = googleMapRef.addCircle(circleOptions);
        circles.add(rangeCircle);
        return false;
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        LatLng latLng = marker.getPosition();
        String address = marker.getTitle();

        if (savedMarkers.contains(marker.getPosition())) {
            savedMarkers.remove(marker.getPosition());
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(chargerMarkerIcon));
            drawPath();

            savedEvStations.removeIf(e -> e.getAddress().equals(address));
            Log.i("evStation", String.valueOf(savedMarkers.size()));
        } else {
            savedMarkers.add(marker.getPosition());
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(savedMarkerIcon));
            drawPath();

            // Add evStation object to the list.
            EvStation evStation = new EvStation(latLng.latitude, latLng.latitude, address);
            Log.i("evStation", evStation.toString());
            savedEvStations.add(evStation);
            Log.i("evStation", String.valueOf(savedMarkers.size()));
        }
        Toast.makeText(getActivity(), "infoWindow clicked", Toast.LENGTH_SHORT).show();
    }

    public void drawPath() {
        // remove previous polyline
        mapPolyline.remove();

        // re-draw polyline with updated points
        mapPolyline = googleMapRef.addPolyline(new PolylineOptions()
                .addAll(savedMarkers)
        );
        mapPolyline.setColor(getResources().getColor(R.color.main_light_blue));
    }

    public void saveStationsToDB() {
        // evStation contains ( Double Double latitude, Double longitude,  String address)
        // Feel free to change anything :)
        ArrayList<EvStation> savedObjets = savedEvStations;
    }
}