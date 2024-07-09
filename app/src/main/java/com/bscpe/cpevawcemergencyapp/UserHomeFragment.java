package com.bscpe.cpevawcemergencyapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserHomeFragment extends Fragment {

    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 123;
    private static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 456;

    public static UserHomeFragment newInstance(String firstName) {
        UserHomeFragment fragment = new UserHomeFragment();
        Bundle args = new Bundle();
        args.putString("firstName", firstName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        // Access the TextView to show the user's first name
        TextView showFirstNameTextView = view.findViewById(R.id.userShowFirstName);
        ImageView emergencyButton = view.findViewById(R.id.mainEmergencyButton);

        // Retrieve the user's first name from the arguments
        Bundle args = getArguments();
        if (args != null) {
            String firstName = args.getString("firstName");
            if (firstName != null) {
                // Set the user's first name in the TextView
                showFirstNameTextView.setText(firstName);
            }
        }

        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmergencySMS();
                callEmergencyHotline();
            }
        });

        return view;
    }

    private void sendEmergencySMS() {
        // Check if the user has granted the SMS permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Request the SMS permission if it hasn't been granted
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        } else {
            // Send the emergency SMS
            if (!sendSMSToEmergencyContact()) {
                // Display an error message as a toast if the SMS sending failed
                Toast.makeText(requireContext(), "Failed to send emergency SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void callEmergencyHotline() {
        // Access the Firebase Realtime Database to fetch the emergency hotline number
        DatabaseReference emergencyHotlineRef = FirebaseDatabase.getInstance().getReference().child("EmergencyHotline").child("Barangay");
        emergencyHotlineRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String emergencyHotlineNumber = dataSnapshot.getValue(String.class);
                // Open the phone dialer with the emergency hotline number
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + emergencyHotlineNumber));
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur
                Toast.makeText(requireContext(), "Failed to fetch emergency hotline number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean sendSMSToEmergencyContact() {
        // Retrieve the user's UID from Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Access the Firebase Realtime Database to fetch the user's emergency contact information
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("UserData").child(userId);
            userRef.child("EmergencyContact").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Retrieve the emergency contact information and send the SMS
                    // You can customize the SMS message and send it to the emergency contacts
                    Toast.makeText(requireContext(), "Hi! This is an EMERGENCY", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle any errors that may occur
                    Toast.makeText(requireContext(), "Failed to send emergency SMS", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return true;
    }
}
