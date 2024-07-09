package com.bscpe.cpevawcemergencyapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;
public class UserProfileEditHealthInfo extends Fragment {

    EditText weight, height, foodAllergies, medicalHistory, surgeries;

    FrameLayout spinner;

    private DatabaseReference mDatabase;
    Spinner bloodType;
    Button submitButton;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile_edit_health_info, container, false);

        bloodType = view.findViewById(R.id.updateBloodTypeSpinner);
        weight = view.findViewById(R.id.updateWeight);
        height = view.findViewById(R.id.updateHeight);
        foodAllergies = view.findViewById(R.id.updateFoodAllergies);
        medicalHistory = view.findViewById(R.id.updateMedicalHistory);
        surgeries = view.findViewById(R.id.updateSurgeries);
        submitButton = view.findViewById(R.id.submitHealthButton);
        spinner = view.findViewById(R.id.progress_loader_health);
        spinner.setVisibility(View.VISIBLE);

        // Initialize Firebase Authentication
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //BLOODTYPE DROPDOWN
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireActivity(), R.array.bloodType, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodType.setAdapter(adapter);


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
                    String userBloodType = dataSnapshot.child("HealthInfo").child("bloodType").getValue(String.class);
                    String userWeight = dataSnapshot.child("HealthInfo").child("weight").getValue(String.class);
                    String userHeight = dataSnapshot.child("HealthInfo").child("height").getValue(String.class);
                    String userFoodAllergies = dataSnapshot.child("HealthInfo").child("foodAllergies").getValue(String.class);
                    String userMedicalHistory = dataSnapshot.child("HealthInfo").child("medicalHistory").getValue(String.class);
                    String userSurgeries = dataSnapshot.child("HealthInfo").child("surgeries").getValue(String.class);

                    spinner.setVisibility(View.GONE);
                    // Set the retrieved data to the EditText and Spinners
                    bloodType.setSelection(((ArrayAdapter) bloodType.getAdapter()).getPosition(userBloodType));
                    weight.setText(userWeight);
                    height.setText(userHeight);
                    foodAllergies.setText(userFoodAllergies);
                    medicalHistory.setText(userMedicalHistory);
                    surgeries.setText(userSurgeries);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                    Log.e("Error", "Database error: " + error.getMessage());
                }
            });
        }

        // Set a click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to update the user's data in the database
                updateUserData();
            }
        });

        return view;
    }

    // Method to update the user's data in the database
    private void updateUserData() {
        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Get the user's ID
            String userId = currentUser.getUid();

            // Get a reference to the "users" node in the database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("UserData").child(userId);

            // Update the user's data in the database
            userRef.child("HealthInfo").child("bloodType").setValue(bloodType.getSelectedItem().toString());
            userRef.child("HealthInfo").child("weight").setValue(weight.getText().toString());
            userRef.child("HealthInfo").child("height").setValue(height.getText().toString());
            userRef.child("HealthInfo").child("foodAllergies").setValue(foodAllergies.getText().toString());
            userRef.child("HealthInfo").child("medicalHistory").setValue(medicalHistory.getText().toString());
            userRef.child("HealthInfo").child("surgeries").setValue(surgeries.getText().toString())
                    .addOnSuccessListener(aVoid -> {
                        // Display a toast message indicating a successful update
                        Toast.makeText(requireContext(), "User data updated successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Handle the failure to update the data
                        Toast.makeText(requireContext(), "Failed to update user data", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}