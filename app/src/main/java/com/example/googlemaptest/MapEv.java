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
import android.widget.Button;
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
import java.util.Locale;
import java.util.Objects;

public class MapEv extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener
        ,GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener
{

    public static final float DEFAULT_RANGE = 450000; // in meters 450km

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    //    ImageView imageViewSearch;
//    EditText locationInput;
    String userId;
    float evRange;
    GoogleMap googleMapRef;
    ArrayList<Circle> circles;
    Bitmap chargerMarkerIcon;
    Bitmap savedMarkerIcon;
    Bitmap mapPinMarkerIcon;
    ArrayList<LatLng> savedMarkers;
    ArrayList<EvStation> savedEvStations;
    Polyline mapPolyline;
    Button saveButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map_ev, container, false);

//        imageViewSearch = view.findViewById(R.id.locationSearch);
//        locationInput = view.findViewById(R.id.locationInput);

        // get userId
        userId = getArguments().getString("uid");
        evRange = getArguments().getFloat("evRange");
        Log.i("userId in MapEv", userId);
        Log.i("hello", String.valueOf(evRange));

        // get the instance of the Firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        // get the reference to the JSON tree
        databaseReference = firebaseDatabase.getReference();

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapEv);

        // Initialize save button and its listener
        saveButton = view.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("Clicked save", "Clicked save");
                saveStationsToDB();
            }
        });
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
        BitmapDrawable bitmapdrawLocation = (BitmapDrawable) getResources().getDrawable( R.drawable.icons8_map_pin_48);
        Bitmap bLocation = bitmapdrawLocation.getBitmap();
        mapPinMarkerIcon = Bitmap.createScaledBitmap(bLocation, 85, 85, false);
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

        // Set click listeners
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnMapLongClickListener(this);

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
                    String city = String.valueOf(snapshot.child("City").getValue());
                    String state = String.valueOf(snapshot.child("State").getValue());
                    String full_address = address + ", " + city + ", " + state;
                    String connectorType = String.valueOf(snapshot.child("EV Connector Types").getValue());
                    LatLng station = new LatLng(lat, lon);

                    // Add markers to the googleMap
                    Objects.requireNonNull(googleMap.addMarker(new MarkerOptions()
                                    .position(station)
                                    .title(full_address)
                                    .snippet("Connector: " + connectorType)))
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
//        Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
        Log.i("Circle center", String.valueOf(marker.getPosition()));

        // remove previous circles
        for (Circle circle: circles) {
            circle.remove();
        }
        circles.clear();

        // draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(marker.getPosition());
        if (evRange == 0) {
            circleOptions.radius(DEFAULT_RANGE);
        } else {
            circleOptions.radius(evRange * 1000); // convert km to m
        }
        circleOptions.strokeColor(0xCC4c8bf5);
        circleOptions.strokeWidth(5);
        circleOptions.fillColor(0x4000A7E1);
        Circle rangeCircle = googleMapRef.addCircle(circleOptions);
        circles.add(rangeCircle);
        return false;
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        LatLng latLng = marker.getPosition();
        String address = marker.getTitle();
        String type = marker.getSnippet();

        // Remove marker from the saved marker list
        if (savedMarkers.contains(marker.getPosition())) {
            savedMarkers.remove(marker.getPosition());
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(chargerMarkerIcon));
            drawPath();

            savedEvStations.removeIf(e -> e.getAddress().equals(address));
            Toast.makeText(getActivity(), address + " removed", Toast.LENGTH_SHORT).show();
            Log.i("evStation", String.valueOf(savedMarkers.size()));
        } else { // Add marker to the saved marker list
            savedMarkers.add(marker.getPosition());
            if (marker.getSnippet().equals("Selected Location")) {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(mapPinMarkerIcon));
            } else {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(savedMarkerIcon));
            }

            drawPath();

            // Add evStation object to the list.
            EvStation evStation = new EvStation(latLng.latitude, latLng.latitude, address, type);
            Log.i("evStation", evStation.toString());
            savedEvStations.add(evStation);
            Toast.makeText(getActivity(), address + " added", Toast.LENGTH_SHORT).show();
            Log.i("evStation", String.valueOf(savedMarkers.size()));
        }

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
        // get the instance of the Firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        // get the reference to the JSON tree
        databaseReference = firebaseDatabase.getReference();

        ArrayList<EvStation> savedObjects = savedEvStations;

        databaseReference.child("Trips").child("creatorId").setValue(userId);
        databaseReference.child("Trips").child("Pins").setValue(savedObjects);
    }


    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            googleMapRef.addMarker(new MarkerOptions().position(latLng).title(address).snippet("Selected Location"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}