package com.bscpe.cpevawcemergencyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bscpe.cpevawcemergencyapp.databinding.ActivityUserHomePageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserHomePage extends AppCompatActivity {

    ActivityUserHomePageBinding binding;

    Dialog dialog;

    Button confirmLogout, cancelLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new Dialog(UserHomePage.this);
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        // Initialize the buttons
        confirmLogout = dialog.findViewById(R.id.confirmLogoutButton);
        cancelLogout = dialog.findViewById(R.id.cancelLogoutButton);

        confirmLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            // go to LandingPage
            Intent intent = new Intent(UserHomePage.this, LandingPage.class);
            startActivity(intent);
            finish();
            Toast.makeText(UserHomePage.this, "Successfully Logged Out", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        cancelLogout.setOnClickListener(v -> {
            dialog.dismiss();
        });

        replaceFragment(new UserHomeFragment());
        binding.bottomUserNavigationView.setBackground(null);
        binding.bottomUserNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.userHome) {
                replaceFragment(new UserHomeFragment());
            } else if (item.getItemId() == R.id.userProfile) {
                replaceFragment(new UserProfileFragment());
            } else if (item.getItemId() == R.id.userChat) {
                //replaceFragment(new ChatFragment());
            } else if (item.getItemId() == R.id.userLogout) {
                //show popup to logout
                dialog.show();
            }
            return true;
        });

        // Fetch and display user data from Firebase Realtime Database
        fetchAndDisplayUserData();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void fetchAndDisplayUserData() {
        // Use Firebase Authentication to get the current user's ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Access the Firebase Realtime Database to fetch user data
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("UserData").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Retrieve user data from the database
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    // ... Retrieve other user data fields

                    // Pass the user data to the fragments
                    Bundle userData = new Bundle();
                    userData.putString("firstName", firstName);
                    userData.putString("lastName", lastName);

                    // ... Put other user data fields into the bundle

                    // Pass the user data to each fragment
                    UserHomeFragment homeFragment = UserHomeFragment.newInstance(firstName);
                    homeFragment.setArguments(userData);
                    replaceFragment(homeFragment);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle any errors that may occur
                    // For example:
                    Toast.makeText(UserHomePage.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
