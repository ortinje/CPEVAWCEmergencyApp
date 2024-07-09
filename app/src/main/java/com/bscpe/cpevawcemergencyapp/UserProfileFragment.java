package com.bscpe.cpevawcemergencyapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class UserProfileFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout viewProfile, userInfo, healthInfo, emergencyContactInfo, securityInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        viewProfile = view.findViewById(R.id.menuViewProfile);
        userInfo = view.findViewById(R.id.menuUserInfo);
        healthInfo = view.findViewById(R.id.menuHealthInfo);
        emergencyContactInfo = view.findViewById(R.id.menuEmergencyContact);
        securityInfo = view.findViewById(R.id.menuSecurity);

        viewProfile.setOnClickListener(this);
        userInfo.setOnClickListener(this);
        healthInfo.setOnClickListener(this);
        emergencyContactInfo.setOnClickListener(this);
        securityInfo.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;

        if (v.getId() == R.id.menuViewProfile) {
            fragment = new UserProfile_View();
        } else if (v.getId() == R.id.menuUserInfo) {
            fragment = new UserProfileEditUserInfo();
        } else if (v.getId() == R.id.menuHealthInfo) {
            fragment = new UserProfileEditHealthInfo();
        } else if (v.getId() == R.id.menuEmergencyContact) {
            //fragment = new MenuEmergencyContact();
        } else if (v.getId() == R.id.menuSecurity) {
            //fragment = new MenuSecurity();
        }


        if (fragment != null) {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
