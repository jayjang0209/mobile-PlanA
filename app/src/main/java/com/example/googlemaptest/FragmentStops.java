package com.example.googlemaptest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class FragmentStops extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> routes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            routes = bundle.getStringArrayList("routes");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyRecyclerViewAdapter myRecyclerViewAdapter;
        View view = inflater.inflate(R.layout.activity_fragment_routes, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), routes);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(myRecyclerViewAdapter);
        return view;
    }}
