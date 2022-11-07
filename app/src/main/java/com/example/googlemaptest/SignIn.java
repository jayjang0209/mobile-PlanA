package com.example.googlemaptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button login = findViewById(R.id.btnSignIn);
        login.setOnClickListener(view -> {
            Intent intent = new Intent(this, EvCharger.class);
            startActivity(intent);
        });

        TextView signUp = findViewById(R.id.goToSignUP);
        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        });
    }
}

// forget password?