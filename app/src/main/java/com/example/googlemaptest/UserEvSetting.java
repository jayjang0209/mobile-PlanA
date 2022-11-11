package com.example.googlemaptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserEvSetting extends AppCompatActivity {
    TextView numObject;
    TextView manufacturer;
    TextView model;
    TextView evRange;
    Spinner evSpinner;
    ArrayList<Ev> evs;

    private final String url = "https://developer.nrel.gov/api/vehicles/v1/light_duty_automobiles.json";
    private final String key = "oeesoXp4Qsx0c1ceqlKtbrFEB7yRujpaTakm9Zul";
    private final String fuel_id = "41";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ev_setting);

        numObject = findViewById(R.id.numDataObject);
        manufacturer = findViewById(R.id.manufacturer);
        model = findViewById(R.id.model);
        evRange =findViewById(R.id.range);
        evSpinner = (Spinner) findViewById(R.id.evSpinner);
        evs = new ArrayList<>();

        UserEvSetting.AsyncTaskRunner runner = new UserEvSetting.AsyncTaskRunner();
        String modelYear = "2022";
        // Sample Endpoint
        // https://developer.nrel.gov/api/vehicles/v1/light_duty_automobiles.json?api_key=oeesoXp4Qsx0c1ceqlKtbrFEB7yRujpaTakm9Zul&current=true&fuel_id=41&model_year=2022
        String endPoint = url + "?api_key=" + key + "&current=true&fuel_id=" + fuel_id + "&model_year=" + modelYear;
        Log.i("endpoint", endPoint);
        runner.execute(endPoint);

        Button authenticate = findViewById(R.id.btnEvTripPlanner);
        authenticate.setOnClickListener(view -> {
            Intent intent = new Intent(this, EvTripPlanner.class);
            startActivity(intent);
        });

    }

    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            RequestQueue queue = Volley.newRequestQueue(UserEvSetting.this);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, strings[0], null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("Response", response.toString());
                        // get the total number of Evs
                        JSONObject jsonObjectCount = response.getJSONObject("metadata");
                        Log.d("Response-result", jsonObjectCount.toString());
                        String numberOfObject = jsonObjectCount.getJSONObject("resultset").getString("count");
                        numObject.setText("number of models: " + numberOfObject);
                        JSONArray jsonArrayEvs = response.getJSONArray("result");
                        Log.d("Response-total-evs-number", String.valueOf(jsonArrayEvs.length()));

                        // add elements in json array to spinner
                        for (int i = 0; i < jsonArrayEvs.length(); i++) {
                            JSONObject jsonObject = jsonArrayEvs.getJSONObject(i);
                            String make = jsonObject.getString("manufacturer_name");
                            String model = jsonObject.getString("model");
                            String model_year = jsonObject.getString("model_year");
                            String range = jsonObject.getString("electric_range");
                            // Instantiate single Ev object
                            Ev ev = new Ev();
                            ev.setManufacturer(make);
                            ev.setModel(model);
                            ev.setYear(model_year);
                            ev.setRange(range);
                            // Add to the ArrayList
                            evs.add(ev);
                        }

                        // Add ArrayAdapter with the list of Evs to Spinner
                        ArrayAdapter<Ev> data = new ArrayAdapter<>(UserEvSetting.this, android.R.layout.simple_spinner_item, evs);
                        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        evSpinner.setAdapter(data);
                        // Add selected listener
                        evSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                // Get the Ev Object representation of the selected item
                                Ev ev = (Ev) parent.getItemAtPosition(position);
                                // set textViews
                                manufacturer.setText(ev.getManufacturer());
                                model.setText(ev.getModel());
                                evRange.setText(String.valueOf((int) ev.getRange()));
                            }

                            // Override the onNothingSelected method defined in AdapterView.OnItemSelectedListener
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Toast.makeText(getApplicationContext(), "Please select ev", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(UserEvSetting.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(request);
            return null;
        }
    }
}