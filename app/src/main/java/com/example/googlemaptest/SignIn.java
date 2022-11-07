package com.example.googlemaptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText edtEmail, edtPassword;
    private String email, password;

    public SignIn() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // get the instance of the Firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        // get the reference to the JSON tree
        databaseReference = firebaseDatabase.getReference();

        edtEmail = findViewById(R.id.editTextEmail);
        edtPassword = findViewById(R.id.editTextPassword);

        Button login = findViewById(R.id.btnSignIn);
        login.setOnClickListener(view -> {
            login();
            Intent intent = new Intent(this, EvCharger.class);
            startActivity(intent);
        }
);

        TextView signUp = findViewById(R.id.goToSignUP);
        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        });
    }

    private void login() {
        // Get user inputs
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();

        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean match = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null && email.equals(user.getEmail()) && password.equals(user.getPassword())) {
                        match = true;
                        Toast.makeText(SignIn.this, "Logged In!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignIn.this, EvCharger.class);
                        startActivity(intent);
                    }
                }
                if (!match) {
                    Toast.makeText(SignIn.this, "Access Denied", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}

// forget password?