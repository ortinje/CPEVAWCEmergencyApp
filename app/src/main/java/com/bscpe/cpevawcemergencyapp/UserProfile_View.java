package com.bscpe.cpevawcemergencyapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserProfile_View extends Fragment {
    TextView showLastName, showFirstName, showBirthday, showSex, showCivilStatus, showContactNumber, showBloodType, showAge, showWeight, showHeight, showFoodAllergies, showMedicalHistory, showSurgeries, showEmergencyContactName01, showEmergencyContactNum01, showEmergencyContactName02, showEmergencyContactNumber02;

    FrameLayout spinner;

    private DatabaseReference mDatabase;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile__view, container, false);

        showLastName = view.findViewById(R.id.showLastName);
        showFirstName = view.findViewById(R.id.showFirstName);
        showBirthday = view.findViewById(R.id.showBirthday);
        showSex = view.findViewById(R.id.showSex);
        showCivilStatus = view.findViewById(R.id.showCivilStatus);
        showContactNumber = view.findViewById(R.id.showContactNumber);
        showBloodType = view.findViewById(R.id.showBloodType);
        showAge = view.findViewById(R.id.showAge);
        showWeight = view.findViewById(R.id.showWeight);
        showHeight = view.findViewById(R.id.showHeight);
        showFoodAllergies = view.findViewById(R.id.showFoodAllergies);
        showMedicalHistory = view.findViewById(R.id.showMedicalHistory);
        showSurgeries = view.findViewById(R.id.showSurgeries);
        showEmergencyContactName01 = view.findViewById(R.id.showEmergencyContactName01);
        showEmergencyContactNum01 = view.findViewById(R.id.showEmergencyContactNum01);
        showEmergencyContactName02 = view.findViewById(R.id.showEmergencyContactName02);
        showEmergencyContactNumber02 = view.findViewById(R.id.showEmergencyContactNumber02);
        spinner = view.findViewById(R.id.progress_loader02);
        spinner.setVisibility(View.VISIBLE);


        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Get the user's ID
            String userId = currentUser.getUid();

            // Get a reference to the "users" node in the database
            mDatabase = FirebaseDatabase.getInstance().getReference().child("UserData").child(userId);
            spinner.setVisibility(View.VISIBLE);
            // Add a listener to retrieve the user's data
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Retrieve user data from the dataSnapshot
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String birthday = dataSnapshot.child("birthdate").getValue(String.class);
                    String sex = dataSnapshot.child("sex").getValue(String.class);
                    String civilStatus = dataSnapshot.child("civilStatus").getValue(String.class);
                    String contactNumber = dataSnapshot.child("contactNum").getValue(String.class);
                    String bloodType = dataSnapshot.child("HealthInfo").child("bloodType").getValue(String.class);
                    String age = dataSnapshot.child("HealthInfo").child("age").getValue(String.class);
                    String weight = dataSnapshot.child("HealthInfo").child("weight").getValue(String.class);
                    String height = dataSnapshot.child("HealthInfo").child("height").getValue(String.class);
                    String foodAllergies = dataSnapshot.child("HealthInfo").child("foodAllergies").getValue(String.class);
                    String medicalHistory = dataSnapshot.child("HealthInfo").child("medicalHistory").getValue(String.class);
                    String surgeries = dataSnapshot.child("HealthInfo").child("surgeries").getValue(String.class);
                    String emergencyContactName01 = dataSnapshot.child("EmergencyContact").child("EmergencyContact01").child("Name").getValue(String.class);
                    String emergencyContactNum01 = dataSnapshot.child("EmergencyContact").child("EmergencyContact01").child("Number").getValue(String.class);
                    String emergencyContactName02 = dataSnapshot.child("EmergencyContact").child("EmergencyContact02").child("Name").getValue(String.class);
                    String emergencyContactNumber02 = dataSnapshot.child("EmergencyContact").child("EmergencyContact02").child("Number").getValue(String.class);

                    spinner.setVisibility(View.GONE);
                    // Set the retrieved data to the TextViews
                    showLastName.setText(lastName);
                    showFirstName.setText(firstName);
                    showBirthday.setText(birthday);
                    showSex.setText(sex);
                    showCivilStatus.setText(civilStatus);
                    showContactNumber.setText(contactNumber);
                    showBloodType.setText(bloodType);
                    showAge.setText(age);
                    showWeight.setText(weight);
                    showHeight.setText(height);
                    showFoodAllergies.setText(foodAllergies);
                    showMedicalHistory.setText(medicalHistory);
                    showSurgeries.setText(surgeries);
                    showEmergencyContactName01.setText(emergencyContactName01);
                    showEmergencyContactNum01.setText(emergencyContactNum01);
                    showEmergencyContactName02.setText(emergencyContactName02);
                    showEmergencyContactNumber02.setText(emergencyContactNumber02);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Log.e("Error", "Database error: " + error.getMessage());
                }

            });
        }

        return view;
    }
}