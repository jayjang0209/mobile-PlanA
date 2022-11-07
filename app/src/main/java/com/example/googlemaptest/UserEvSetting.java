package com.example.googlemaptest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
//https://stackoverflow.com/questions/1625249/android-how-to-bind-spinner-to-custom-object-list
    TextView numObject;
    Spinner evSpinner;
    ArrayList<String> evs;
    TextView evRange;


    private final String url = "https://developer.nrel.gov/api/vehicles/v1/light_duty_automobiles.json";
    private final String key = "oeesoXp4Qsx0c1ceqlKtbrFEB7yRujpaTakm9Zul";
    private final String fuel_id = "41";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ev_setting);

        Log.i("here", "hello");
        numObject = findViewById(R.id.numDataObject);
        evSpinner = (Spinner) findViewById(R.id.evSpinner);
        evRange =findViewById(R.id.textRange);
        evs = new ArrayList<>();

        numObject.setText("Test!!");
        UserEvSetting.AsyncTaskRunner runner = new UserEvSetting.AsyncTaskRunner();
        String modelYear = "2022";
        // Sample Endpoint
        // https://developer.nrel.gov/api/vehicles/v1/light_duty_automobiles.json?api_key=oeesoXp4Qsx0c1ceqlKtbrFEB7yRujpaTakm9Zul&current=true&fuel_id=41&model_year=2022
        String endPoint = url + "?api_key=" + key + "&current=true&fuel_id=" + fuel_id + "&model_year=" + modelYear;
        Log.i("endpoint", endPoint);
        runner.execute(endPoint);

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
                        JSONObject jsonObjectCount = response.getJSONObject("metadata");
                        Log.d("Response-result", jsonObjectCount.toString());
                        String numberOfObject = jsonObjectCount.getJSONObject("resultset").getString("count");
                        numObject.setText(numberOfObject);
                        JSONArray jsonArrayEvs = response.getJSONArray("result");
                        Log.d("Response-evs", String.valueOf(jsonArrayEvs.length()));

                        // add elements in json array to spinner
                        for (int i = 0; i < jsonArrayEvs.length(); i++) {
                            JSONObject jsonObject = jsonArrayEvs.getJSONObject(i);
                            String make = jsonObject.getString("manufacturer_name");
                            String model = jsonObject.getString("model");
                            String model_year = jsonObject.getString("model_year");
                            String range = jsonObject.getString("electric_range");
                            String ev = i + make + " " + model + " " + model_year + " " + range;
                            Log.d("Response-ev", ev);

                            evs.add(make + "-" + model);
                        }


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