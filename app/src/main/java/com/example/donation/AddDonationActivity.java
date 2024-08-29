package com.example.donation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AddDonationActivity extends AppCompatActivity {
    private EditText editTextDate, editTextUnits;
    private Button buttonSaveDonation;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donation);

        db = new DatabaseHelper(this);

        editTextDate = findViewById(R.id.editTextDate);
        editTextUnits = findViewById(R.id.editTextUnits);
        buttonSaveDonation = findViewById(R.id.buttonSaveDonation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        }


        buttonSaveDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = editTextDate.getText().toString();
                int units = Integer.parseInt(editTextUnits.getText().toString());
                String email = getIntent().getStringExtra("EMAIL");

                long result = db.addDonation(email, date, units);
                if (result > 0) {
                    Toast.makeText(AddDonationActivity.this, "Donation saved!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddDonationActivity.this, "Failed to save donation.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
