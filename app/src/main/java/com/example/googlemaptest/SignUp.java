package com.example.googlemaptest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private EditText edtFirstName, edtLastName, edtPassword, edtEmail, edtConfirmPassword;
    private String firstName;
    private String lastName;
    private String name;
    private String password;
    private String email;
    private DatabaseReference databaseReference;
    private Button btnSignup;
    private Bundle bundle;

    public SignUp() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // get the instance of the Firebase database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // get the reference to the JSON tree
        databaseReference = firebaseDatabase.getReference();

        edtFirstName = findViewById(R.id.editFirstName);
        edtLastName = findViewById(R.id.editLastName);
        edtEmail = findViewById(R.id.editTextEmail);
        edtPassword = findViewById(R.id.editTextPassword);
        edtConfirmPassword = findViewById(R.id.editTextRePassword);

        isEVDriver();
    }

    private boolean checkFields() {
        firstName = edtFirstName.getText().toString().trim();
        lastName = edtLastName.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Please enter a first name", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Please enter a last name", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter a last name", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please confirm the password", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Please re-enter to confirm the password", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void isEVDriver() {
        CheckBox checkBox = findViewById(R.id.is_ev_driver);
        btnSignup = findViewById(R.id.btnCreateAccount);

        btnSignup.setOnClickListener(view -> {
            if (checkFields()) {
                addData();
            }
        });

        checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                btnSignup.setText(R.string.next);
                btnSignup.setOnClickListener(view -> {
                    if (checkFields()) {
                        Intent intent = new Intent(this, UserEvSetting.class);
                        bundle = new Bundle();
                        name = firstName + " " + lastName;
                        bundle.putString("name", name);
                        bundle.putString("email", email);
                        bundle.putString("password", password);
                        intent.putExtra("bundle", bundle);
                        startActivity(intent);
                    }
                });
            }
            if (!isChecked) {
                btnSignup.setText(R.string.create_account);
                btnSignup.setOnClickListener(view -> {
                    if (checkFields()) {
                        addData();
                    }
                });
            }
        });
    }

    private void addData() {
        // use push method to generate a unique key for a new child node
        String id = databaseReference.push().getKey();
        writeNewUser(id);
    }

    private void writeNewUser(String id) {
        name = firstName + " " + lastName;
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        databaseReference.child("Users").child(id).setValue(user)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(SignUp.this, "Account created", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, SignIn.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(SignUp.this, e.toString(), Toast.LENGTH_LONG).show());
    }
}