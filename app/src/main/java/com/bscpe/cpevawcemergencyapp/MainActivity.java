package com.bscpe.cpevawcemergencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("AdminCreadentials");

        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // User is already signed in, check if admin
            checkAdminStatus(currentUser.getEmail());
        } else {
            // User is not signed in, proceed to LandingPage
            startActivity(new Intent(MainActivity.this, LandingPage.class));
            finish();
        }
    }

    private void checkAdminStatus(String email) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String adminEmail = childSnapshot.getValue(String.class);
                        if (email.equals(adminEmail)) {
                            // User is an admin, check if email is verified and go to AdminHomePage
                            if (currentUser.isEmailVerified()) {
                                startActivity(new Intent(MainActivity.this, AdminHomePage.class));
                                finish();
                            } else {
                                // User is an admin but email is not verified, stay on MainActivity
                                startActivity(new Intent(MainActivity.this, LandingPage.class));
                                finish();
                            }
                            return;
                        }
                    }
                }

                // User is not an admin, check if email is verified and go to UserHomePage
                if (currentUser.isEmailVerified()) {
                    startActivity(new Intent(MainActivity.this, UserHomePage.class));
                    finish();
                } else {
                    // User is not an admin and email is not verified, stay on MainActivity
                    startActivity(new Intent(MainActivity.this, LandingPage.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Log.e("Error", "Database error: " + error.getMessage());
            }
        });
    }
}
