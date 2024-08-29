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

public class EditProfileActivity extends AppCompatActivity {

    EditText etName, etEmail, etAge;
    AutoCompleteTextView autoCompleteBloodGroup;
    Button btnSave;
    DatabaseHelper db;
    String userEmail;

    String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etAge = findViewById(R.id.etAge);
        autoCompleteBloodGroup = findViewById(R.id.autoCompleteBloodGroup);
        btnSave = findViewById(R.id.btnSave);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, bloodGroups);
        autoCompleteBloodGroup.setAdapter(adapter);
        autoCompleteBloodGroup.setThreshold(1); // Start suggesting from the first character

        db = new DatabaseHelper(this);

        userEmail = getIntent().getStringExtra("EMAIL");
        loadUserProfile(userEmail);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    saveUserProfile();
                }
            }
        });
    }

    private void loadUserProfile(String email) {
        User user = db.getUser(email);
        etName.setText(user.getName());
        etEmail.setText(user.getEmail());
        etAge.setText(String.valueOf(user.getAge()));
        autoCompleteBloodGroup.setText(user.getBloodGroup());
    }

    private boolean validateInput() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String bloodGroup = autoCompleteBloodGroup.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || ageStr.isEmpty() || bloodGroup.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean validBloodGroup = false;
        for (String bg : bloodGroups) {
            if (bg.equals(bloodGroup)) {
                validBloodGroup = true;
                break;
            }
        }

        if (!validBloodGroup) {
            Toast.makeText(this, "Please enter a valid blood group", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveUserProfile() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        int age = Integer.parseInt(etAge.getText().toString().trim());
        String bloodGroup = autoCompleteBloodGroup.getText().toString().trim();

        // Update the user information
        boolean isUpdated = db.updateUser(userEmail, name, email, age, bloodGroup);

        if (isUpdated) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

            // Update the userEmail if the email was changed
            userEmail = email;

            Intent resultIntent = new Intent();
            resultIntent.putExtra("EMAIL", email);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }
}
