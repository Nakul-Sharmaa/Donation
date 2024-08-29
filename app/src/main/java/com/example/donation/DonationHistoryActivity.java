package com.example.donation;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class DonationHistoryActivity extends AppCompatActivity {
    ListView listView;
    DatabaseHelper db;
    ArrayList<String> donationHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history);

        listView = findViewById(R.id.lvDonationHistory);
        db = new DatabaseHelper(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        }


        String email = getIntent().getStringExtra("EMAIL");
        loadDonationHistory(email);
    }

    private void loadDonationHistory(String email) {
        ArrayList<DonationRecord> records = db.getDonationHistory(email);
        donationHistory = new ArrayList<>();

        for (DonationRecord record : records) {
            donationHistory.add("Date: " + record.getDate() + ", Units: " + record.getUnits());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, donationHistory);
        listView.setAdapter(adapter);
    }
}
