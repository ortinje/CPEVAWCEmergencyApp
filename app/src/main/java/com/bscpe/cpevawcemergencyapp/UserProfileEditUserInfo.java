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
public class UserProfileEditUserInfo extends Fragment {

    EditText lastName, firstName, birthday, contactNumber, address;

    FrameLayout spinner;

    private DatabaseReference mDatabase;
    Spinner sex, civilStatus;
    Button submitButton;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile_edit_user_info, container, false);

        lastName = view.findViewById(R.id.updateLastName);
        firstName = view.findViewById(R.id.updateFirstName);
        birthday = view.findViewById(R.id.updateBirthdate);
        sex = view.findViewById(R.id.updateSexSpinner);
        civilStatus = view.findViewById(R.id.updateCivilStatusSpinner);
        contactNumber = view.findViewById(R.id.updateContactNum);
        address = view.findViewById(R.id.updateRegAddress);
        submitButton = view.findViewById(R.id.submitButton);
        spinner = view.findViewById(R.id.progress_loader03);
        spinner.setVisibility(View.VISIBLE);

        // Initialize Firebase Authentication
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //SEX DROPDOWN
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireActivity(), R.array.sex, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex.setAdapter(adapter);

        //CIVIL STATUS
        ArrayAdapter<CharSequence> civilStatusAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.civilStatus, R.layout.spinner_item);
        civilStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        civilStatus.setAdapter(civilStatusAdapter);

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
                        R.style.DatePickerDialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Update the EditText with the selected date in MM/DD/YYYY format
                                String selectedDate = String.format(Locale.US, "%02d/%02d/%04d", month + 1, dayOfMonth, year);
                                birthday.setText(selectedDate);
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

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
                    String userLastName = dataSnapshot.child("lastName").getValue(String.class);
                    String userFirstName = dataSnapshot.child("firstName").getValue(String.class);
                    String userBirthday = dataSnapshot.child("birthdate").getValue(String.class);
                    String userSex = dataSnapshot.child("sex").getValue(String.class);
                    String userCivilStatus = dataSnapshot.child("civilStatus").getValue(String.class);
                    String userContactNumber = dataSnapshot.child("contactNum").getValue(String.class);
                    String userAddress = dataSnapshot.child("address").getValue(String.class);

                    spinner.setVisibility(View.GONE);
                    // Set the retrieved data to the EditText and Spinners
                    lastName.setText(userLastName);
                    firstName.setText(userFirstName);
                    birthday.setText(userBirthday);
                    sex.setSelection(((ArrayAdapter) sex.getAdapter()).getPosition(userSex));
                    civilStatus.setSelection(((ArrayAdapter) civilStatus.getAdapter()).getPosition(userCivilStatus));
                    contactNumber.setText(userContactNumber);
                    address.setText(userAddress);
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

    private int getAge(int year, int month, int day) {
        Calendar dateOfBirth = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dateOfBirth.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dateOfBirth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
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

            // Extract the birthday date
            String birthdateString = birthday.getText().toString();
            String[] birthdateParts = birthdateString.split("/");
            int year = Integer.parseInt(birthdateParts[2]);
            int month = Integer.parseInt(birthdateParts[0]) - 1; // Months are 0-based
            int day = Integer.parseInt(birthdateParts[1]);

            // Calculate the age
            int age = getAge(year, month, day);
            String ageString = String.valueOf(age);
            // Update the user's data in the database
            userRef.child("lastName").setValue(lastName.getText().toString());
            userRef.child("firstName").setValue(firstName.getText().toString());
            userRef.child("birthdate").setValue(birthday.getText().toString());
            userRef.child("HealthInfo").child("age").setValue(ageString);
            userRef.child("sex").setValue(sex.getSelectedItem().toString());
            userRef.child("civilStatus").setValue(civilStatus.getSelectedItem().toString());
            userRef.child("contactNum").setValue(contactNumber.getText().toString());
            userRef.child("address").setValue(address.getText().toString())
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