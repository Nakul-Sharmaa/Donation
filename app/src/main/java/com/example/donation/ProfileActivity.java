package com.example.donation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int EDIT_PROFILE_REQUEST = 1;

    TextView tvName, tvEmail, tvAge, tvBloodGroup, tvDonationSuggestion, tvNextDonationDate;
    ImageView profileImage;
    EditText etDonationDate, etDonationUnits;
    DatabaseHelper db;
    Button btnLogout, btnAddDonation;
    SharedPreferences sharedPreferences;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        }




        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Get header view and its text views
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderName = headerView.findViewById(R.id.nav_header_name);
        TextView navHeaderEmail = headerView.findViewById(R.id.nav_header_email);

        profileImage = findViewById(R.id.profile_image);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvAge = findViewById(R.id.tvAge);
        tvBloodGroup = findViewById(R.id.tvBloodGroup);
        tvDonationSuggestion = findViewById(R.id.tvDonationSuggestion);
        tvNextDonationDate = findViewById(R.id.tvNextDonationDate);
        etDonationDate = findViewById(R.id.etDonationDate);
        etDonationUnits = findViewById(R.id.etDonationUnits);
        btnAddDonation = findViewById(R.id.btnAddDonation);
        btnLogout = findViewById(R.id.btnLogout);

        db = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        userEmail = getIntent().getStringExtra("EMAIL");
        loadUserProfile(userEmail);

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.putString("userEmail", "");
            editor.apply();

            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnAddDonation.setOnClickListener(v -> addDonation());

        // Set navigation header values
        User user = db.getUser(userEmail);
        if (user != null) {
            navHeaderName.setText(user.getName());
            navHeaderEmail.setText(user.getEmail());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String itemTitle = item.getTitle().toString();
        if (itemTitle.equals(getString(R.string.nav_edit_profile))) {
            Intent editIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            editIntent.putExtra("EMAIL", userEmail);
            startActivityForResult(editIntent, EDIT_PROFILE_REQUEST);
        } else if (itemTitle.equals(getString(R.string.nav_view_history))) {
            Intent historyIntent = new Intent(ProfileActivity.this, DonationHistoryActivity.class);
            historyIntent.putExtra("EMAIL", userEmail);
            startActivity(historyIntent);
        }
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK && data != null) {
            userEmail = data.getStringExtra("EMAIL");
            loadUserProfile(userEmail);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile(userEmail);
    }

    private void loadUserProfile(String email) {
        User user = db.getUser(email);

        tvName.setText("Name: " + user.getName());
        tvEmail.setText("Email: " + user.getEmail());
        tvAge.setText("Age: " + user.getAge());
        tvBloodGroup.setText("Blood Group: " + user.getBloodGroup());
        tvDonationSuggestion.setText("Suggested Donation: " + calculateDonationSuggestion(user.getBloodGroup(), user.getAge()));
        tvNextDonationDate.setText("Next Donation Date: " + getNextDonationDate());
    }

    private String calculateDonationSuggestion(String bloodGroup, int age) {
        int maxUnits = 0;
        if (age >= 18 && age <= 60) {
            switch (bloodGroup) {
                case "A+":
                case "O+":
                case "B+":
                case "AB+":
                case "A-":
                case "O-":
                case "B-":
                case "AB-":
                    maxUnits = 3;
                    break;
            }
        }
        return maxUnits + " units";
    }

    private String getNextDonationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 15);
        return String.format("%1$tY-%1$tm-%1$td", calendar);
    }
    private void addDonation() {
        String date = etDonationDate.getText().toString().trim();
        String unitsStr = etDonationUnits.getText().toString().trim();

        if (date.isEmpty() || unitsStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int units = Integer.parseInt(unitsStr);
            long result = db.addDonation(userEmail, date, units);

            if (result != -1) {
                etDonationDate.setText("");
                etDonationUnits.setText("");
                tvNextDonationDate.setText("Next Donation Date: " + getNextDonationDate());
                Toast.makeText(this, "Donation added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error adding donation", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }
}
