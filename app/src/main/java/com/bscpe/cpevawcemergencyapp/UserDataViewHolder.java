package com.bscpe.cpevawcemergencyapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserDataViewHolder extends RecyclerView.ViewHolder {

    public TextView usernameTv, contactTv;
    public UserDataViewHolder(@NonNull View itemView) {
        super(itemView);
        usernameTv = itemView.findViewById(R.id.username_tv);
        contactTv = itemView.findViewById(R.id.contact_tv);
    }
}
