package com.example.womensafetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class LocationActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        getSupportActionBar().hide();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.location);

        firebaseAuth = FirebaseAuth.getInstance();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomePage.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.location:
                        startActivity(new Intent(getApplicationContext(), LocationActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.alert:
                        startActivity(new Intent(getApplicationContext(), AlertPage.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.editProfile:
                        startActivity(new Intent(getApplicationContext(), EditProfile.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.logout:
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }
}