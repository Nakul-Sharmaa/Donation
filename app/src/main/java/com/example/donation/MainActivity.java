package com.example.donation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {
    Button btnRegister, btnLogin;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        // Check if user is already logged in
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            String email = sharedPreferences.getString("userEmail", "");
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("EMAIL", email);
            startActivity(intent);
            finish();
        }

        btnRegister.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));

        btnLogin.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
    }
}
