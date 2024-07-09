package com.bscpe.cpevawcemergencyapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdminHomeFragment extends Fragment {

    // Method to create a new instance of HomeFragment and set arguments
    public static AdminHomeFragment newInstance(String firstName) {
        AdminHomeFragment fragment = new AdminHomeFragment();
        Bundle args = new Bundle();
        args.putString("firstName", firstName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        // Access the TextView to show the user's first name
        TextView showFirstNameTextView = view.findViewById(R.id.showFirstName);
        LinearLayout goToAdminUserDataTableTextView = view.findViewById(R.id.gotoAdminUserDataTable);
        // Retrieve the user's first name from the arguments
        Bundle args = getArguments();
        if (args != null) {
            String firstName = args.getString("firstName");
            if (firstName != null) {
                // Set the user's first name in the TextView
                showFirstNameTextView.setText(firstName);
            }
        }

        // Handle the click event for the "Go to Admin User Data Table" button
        goToAdminUserDataTableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the AdminUserDataTable activity
                Intent intent = new Intent(getActivity(), AdminUserDataTable.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
