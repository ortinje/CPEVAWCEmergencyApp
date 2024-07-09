package com.bscpe.cpevawcemergencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class UserLoginPage extends AppCompatActivity {

    LinearLayout changeUserBtn;
    TextView gotoUserRegisterBtn;
    Button userLoginBtn;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference adminCredentialsRef;
    EditText userLoginEmail, userLoginPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_page);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        adminCredentialsRef = database.getReference("AdminCreadentials");

        changeUserBtn = findViewById(R.id.changeUserButton);
        gotoUserRegisterBtn = findViewById(R.id.gotoUserRegisterButton);
        userLoginBtn = findViewById(R.id.userLoginButton);
        userLoginEmail = findViewById(R.id.userLoginEmail);
        userLoginPassword = findViewById(R.id.userLoginPassword);

        // Go to LandingPage
        changeUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserLoginPage.this, LandingPage.class));
            }
        });

        //Go to Admin Signup Page
        gotoUserRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserLoginPage.this, UserSignUpPage.class));
            }
        });

        //Login
        userLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userLoginEmail.getText().toString().trim();
                String password = userLoginPassword.getText().toString().trim();

                // Check if the email is not in the "AdminCreadentials" database
                adminCredentialsRef.orderByValue().equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(UserLoginPage.this, "Email is registered as admin.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Email is not in the "AdminCreadentials" database, show a toast
                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                if (user.isEmailVerified()) {
                                                    // Email is verified, go to AdminHomePage
                                                    startActivity(new Intent(UserLoginPage.this, UserHomePage.class));
                                                } else {
                                                    // Email is not verified, show a toast
                                                    Toast.makeText(UserLoginPage.this, "Email is not verified. Please verify your email.", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                // Login failed, show a toast
                                                Toast.makeText(UserLoginPage.this, "Authentication failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                        Log.e("Error", "Database error: " + error.getMessage());
                    }
                });
            }
        });
    }
}
