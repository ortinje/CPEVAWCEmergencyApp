package com.bscpe.cpevawcemergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LandingPage extends AppCompatActivity {

    ImageView emergencyBtn;
    Button contactAdminBtn, generalUserBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        emergencyBtn = findViewById(R.id.emergencyButton);
        contactAdminBtn = findViewById(R.id.contactAdminButton);
        generalUserBtn = findViewById(R.id.generalUserButton);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("EmergencyHotline/Barangay");

        //call barangay official emergency number saved in firebase database
        emergencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String phoneNumber = dataSnapshot.getValue(String.class);
                        if (phoneNumber != null) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + phoneNumber));
                            startActivity(intent);
                        } else {
                            Log.e("Error", "Phone number is null");
                            Toast.makeText(getApplicationContext(), "Error: Phone number is null", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Error", "Database error: " + databaseError.getMessage());
                        Toast.makeText(getApplicationContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        //go to admin Login
        contactAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingPage.this, AdminLoginPage.class));
            }
        });

        //go to user login page
        generalUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingPage.this, UserLoginPage.class));
            }
        });
    }
}