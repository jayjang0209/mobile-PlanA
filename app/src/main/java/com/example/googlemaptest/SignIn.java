package com.example.googlemaptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button authenticate = findViewById(R.id.btnSignIn);
        authenticate.setOnClickListener(view -> {
            Intent intent = new Intent(this, Map.class);
            startActivity(intent);
        });
    }
}