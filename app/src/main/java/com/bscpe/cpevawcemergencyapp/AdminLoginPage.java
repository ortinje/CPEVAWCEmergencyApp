package com.bscpe.cpevawcemergencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginPage extends AppCompatActivity {

    LinearLayout changeUserBtn;
    TextView gotoRegisterBtn;
    Button loginBtn;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference adminCredentialsRef;
    EditText loginEmail, loginPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_page);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        adminCredentialsRef = database.getReference("AdminCreadentials");

        changeUserBtn = findViewById(R.id.changeUserButton);
        gotoRegisterBtn = findViewById(R.id.gotoRegisterButton);
        loginBtn = findViewById(R.id.loginButton);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);

        // Go to LandingPage
        changeUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminLoginPage.this, LandingPage.class));
            }
        });

        //Go to Admin Signup Page
        gotoRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminLoginPage.this, AdminSignUpPage.class));
            }
        });

        //Login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                // Check if the email is in the "AdminCreadentials" database
                adminCredentialsRef.orderByValue().equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Email is in the "AdminCreadentials" database, proceed to check if it's verified
                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                if (user.isEmailVerified()) {
                                                    // Email is verified, go to AdminHomePage
                                                    startActivity(new Intent(AdminLoginPage.this, AdminHomePage.class));
                                                } else {
                                                    // Email is not verified, show a toast
                                                    Toast.makeText(AdminLoginPage.this, "Email is not verified. Please verify your email.", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                // Login failed, show a toast
                                                Toast.makeText(AdminLoginPage.this, "Authentication failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Email is not in the "AdminCreadentials" database, show a toast
                            Toast.makeText(AdminLoginPage.this, "Email is not registered as an admin.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                        Toast.makeText(AdminLoginPage.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
