package com.bscpe.cpevawcemergencyapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;


public class AdminUserDataTable extends AppCompatActivity {

    Spinner sexDropdown, civilStatusDropdown, bloodType;
    EditText birthdayInput,emailInput, passwordInput, repeatPassInput, lastNameInput, firstNameInput,contactNumInput, addressInput,weight, height, foodAllergies, medicalHistory, surgeries;
    Button submitBtn;

    ProgressBar spinner;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_data_table);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sexDropdown = findViewById(R.id.userSexSpinner_Admin);
        civilStatusDropdown = findViewById(R.id.userCivilStatusSpinner_Admin);
        birthdayInput = findViewById(R.id.userBirthdate_Admin);
        submitBtn = findViewById(R.id.userSubmitButton_Admin);
        emailInput = findViewById(R.id.userRegEmail_Admin);
        passwordInput = findViewById(R.id.userRegPassword_Admin);
        repeatPassInput = findViewById(R.id.userRegPasswordConfirm_Admin);
        firstNameInput = findViewById(R.id.userRegFirstName_Admin);
        lastNameInput = findViewById(R.id.userRegLastName_Admin);
        contactNumInput= findViewById(R.id.userRegContactNum_Admin);
        addressInput = findViewById(R.id.userRegAddress_Admin);
        bloodType = findViewById(R.id.updateBloodTypeSpinner_Admin);
        weight = findViewById(R.id.updateWeight_Admin);
        height = findViewById(R.id.updateHeight_Admin);
        foodAllergies = findViewById(R.id.updateFoodAllergies_Admin);
        medicalHistory = findViewById(R.id.updateMedicalHistory_Admin);
        surgeries = findViewById(R.id.updateSurgeries_Admin);
        spinner = findViewById(R.id.user_progress_loader_progressbar_Admin);
        spinner.setVisibility(View.GONE);
        //SEX DROPDOWN
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sex, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexDropdown.setAdapter(adapter);

        //BLOODTYPE DROPDOWN
        ArrayAdapter<CharSequence> healthAdapter = ArrayAdapter.createFromResource(this, R.array.bloodType, R.layout.spinner_item);
        healthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodType.setAdapter(healthAdapter);

        //CIVIL STATUS
        ArrayAdapter<CharSequence> civilStatusAdapter = ArrayAdapter.createFromResource(this, R.array.civilStatus, R.layout.spinner_item);
        civilStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        civilStatusDropdown.setAdapter(civilStatusAdapter);

        //BIRTDATE DATE PICKER
        birthdayInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminUserDataTable.this,
                        R.style.DatePickerDialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Update the EditText with the selected date in MM/DD/YYYY format
                                String selectedDate = String.format(Locale.US, "%02d/%02d/%04d", month + 1, dayOfMonth, year);
                                birthdayInput.setText(selectedDate);
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String confirmPassword = repeatPassInput.getText().toString().trim();
                String firstName = firstNameInput.getText().toString().trim();
                String lastName = lastNameInput.getText().toString().trim();
                String contactNum = contactNumInput.getText().toString().trim();
                String address = addressInput.getText().toString().trim();
                String birthdate = birthdayInput.getText().toString();
                String sex = sexDropdown.getSelectedItem().toString();
                String civilStatus = civilStatusDropdown.getSelectedItem().toString();
                String userBloodType = bloodType.getSelectedItem().toString();
                String userWeight = weight.getText().toString();
                String userHeight = height.getText().toString();
                String userFoodAllergies = foodAllergies.getText().toString();
                String userMedicalHistory = medicalHistory.getText().toString();
                String userSurgeries = surgeries.getText().toString();

                //check if email is not empty and email is valid
                if (TextUtils.isEmpty(email) || !isValidEmail(email)) {
                    Toast.makeText(AdminUserDataTable.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(AdminUserDataTable.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(AdminUserDataTable.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 8) {
                    Toast.makeText(AdminUserDataTable.this, "Password should be at least 8 characters long", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(firstName)) {
                    Toast.makeText(AdminUserDataTable.this, "Please input first name", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(lastName)) {
                    Toast.makeText(AdminUserDataTable.this, "Please input last name", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(contactNum)) {
                    Toast.makeText(AdminUserDataTable.this, "Please input contact number", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(AdminUserDataTable.this, "Please input address", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(birthdate)) {
                    Toast.makeText(AdminUserDataTable.this, "Please input birthday", Toast.LENGTH_SHORT).show();
                }

                spinner.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                spinner.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                String userId = user.getUid();

                                                // Save user data to Firebase Realtime Database
                                                DatabaseReference userRef = mDatabase.child("UserData").child(userId);
                                                userRef.child("email").setValue(email);
                                                userRef.child("lastName").setValue(lastName);
                                                userRef.child("firstName").setValue(firstName);
                                                userRef.child("birthdate").setValue(birthdate);
                                                userRef.child("sex").setValue(sex);
                                                userRef.child("civilStatus").setValue(civilStatus);
                                                userRef.child("contactNum").setValue(contactNum);
                                                userRef.child("address").setValue(address);
                                                userRef.child("HealthInfo").child("bloodType").setValue(userBloodType);
                                                userRef.child("HealthInfo").child("weight").setValue(userWeight);
                                                userRef.child("HealthInfo").child("height").setValue(userHeight);
                                                userRef.child("HealthInfo").child("foodAllergies").setValue(userFoodAllergies);
                                                userRef.child("HealthInfo").child("medicalHistory").setValue(userMedicalHistory);
                                                userRef.child("HealthInfo").child("surgeries").setValue(userSurgeries);


                                                Toast.makeText(AdminUserDataTable.this, "User registered successfully. Please proceed with email verification",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), AdminUserDataTable.class);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                Toast.makeText(AdminUserDataTable.this, "Authentication failed. Please try again or contact admin",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(AdminUserDataTable.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }


            private boolean isValidEmail(String email) {
                return Patterns.EMAIL_ADDRESS.matcher(email).matches();
            }
        });



    }
}