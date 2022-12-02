package com.example.googlemaptest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;


public class Budget extends AppCompatActivity {
    PieChart pieChart;
    int[] colorArray = new int[]{Color.LTGRAY, Color.BLUE, Color.RED, Color.GREEN};
    EditText flight_amount;
    EditText transportation_amount;
    EditText accommodation_amount;
    EditText food_amount;
    Button btn_apply;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_manager);

        pieChart = findViewById(R.id.piechart);
        flight_amount = findViewById(R.id.edt_flight);
        transportation_amount = findViewById(R.id.edt_transportation);
        accommodation_amount = findViewById(R.id.edt_accommodation);
        food_amount = findViewById(R.id.edt_food);
        btn_apply = findViewById(R.id.btn_apply);

        // Get current user
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        ArrayList<PieEntry> dataValue = new ArrayList<>();
        dataValue.add(new PieEntry(100, "Enter your budget"));

        PieDataSet pieDataSet = new PieDataSet(dataValue, "Budget");
        pieDataSet.setColors(colorArray);
        PieData pieData = new PieData(pieDataSet);
        Description des = new Description();
        des.setText("");
        pieChart.setDrawEntryLabels(true);
        pieChart.setUsePercentValues(false);
        pieData.setValueTextSize(30);
        pieChart.setCenterText("Budget");
        pieChart.setCenterTextSize(20);
        pieChart.setHoleRadius(30);
        pieChart.setData(pieData);
        pieChart.setDescription(des);
        pieChart.invalidate();

        btn_apply.setOnClickListener(view -> {
            System.out.println("Clicked!");
            PieDataSet updated = new PieDataSet(inputData(), "");
            updated.setColors(colorArray);
            PieData new_pieData = new PieData(updated);
            pieChart.setData(new_pieData);
            new_pieData.setValueTextSize(30);
            new_pieData.setValueTextColor(Color.WHITE);
            pieChart.setDrawEntryLabels(true);
            pieChart.setUsePercentValues(false);
            pieChart.setCenterText("Budget");
            pieChart.setCenterTextSize(20);
            pieChart.setHoleRadius(30);
            pieChart.setDescription(des);
            pieChart.invalidate();
        });

    }

    private ArrayList<PieEntry> inputData() {
        ArrayList<PieEntry> dataValue = new ArrayList<>();

        try {
            int flight = Integer.parseInt(flight_amount.getText().toString());
            int transportation = Integer.parseInt(transportation_amount.getText().toString());
            int accommodation = Integer.parseInt(accommodation_amount.getText().toString());
            int food = Integer.parseInt(food_amount.getText().toString());

            dataValue.add(new PieEntry(flight, "Flight"));
            dataValue.add(new PieEntry(transportation, "Transportation"));
            dataValue.add(new PieEntry(accommodation, "Accommodation"));
            dataValue.add(new PieEntry(food, "Food"));
        } catch (NumberFormatException ex) {
            return dataValue;
        }
        return dataValue;
    }
}
