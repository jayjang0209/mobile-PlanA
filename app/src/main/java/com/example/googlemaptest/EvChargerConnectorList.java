package com.example.googlemaptest;

import androidx.fragment.app.ListFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


public class EvChargerConnectorList extends ListFragment {

    String[] connectors;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectors = getResources().getStringArray(R.array.connectors);
        // before findById, but now why? getResources?
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ev_charger_connector_list, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_list_item_1, connectors);
        setListAdapter(adapter);
        return view;
    }
}