package com.example.googlemaptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.Set;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button authenticate = findViewById(R.id.btnCreateAccount);
        authenticate.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserEvSetting.class);
            startActivity(intent);
        });
    }
}