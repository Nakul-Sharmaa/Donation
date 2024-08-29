package com.example.donation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class RegisterActivity extends AppCompatActivity {
    EditText etName, etEmail, etPassword, etAge;
    AutoCompleteTextView autoCompleteBloodGroup;
    Button btnRegister;
    DatabaseHelper db;

    String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etAge = findViewById(R.id.etAge);
        autoCompleteBloodGroup = findViewById(R.id.autoCompleteBloodGroup);
        btnRegister = findViewById(R.id.btnRegister);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        }


        db = new DatabaseHelper(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, bloodGroups);
        autoCompleteBloodGroup.setAdapter(adapter);
        autoCompleteBloodGroup.setThreshold(1); // Start suggesting from the first character

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                int age = Integer.parseInt(etAge.getText().toString());
                String bloodGroup = autoCompleteBloodGroup.getText().toString();

                // Validate blood group input
                boolean validBloodGroup = false;
                for (String bg : bloodGroups) {
                    if (bg.equals(bloodGroup)) {
                        validBloodGroup = true;
                        break;
                    }
                }

                if (!validBloodGroup) {
                    Toast.makeText(RegisterActivity.this, "Please enter a valid blood group", Toast.LENGTH_SHORT).show();
                    return;
                }

                long res = db.addUser(name, email, password, age, bloodGroup);
                if (res > 0) {
                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
