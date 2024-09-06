package com.example.githubbrowser.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.githubbrowser.R;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // setting up setting button
        ImageButton settingButton = view.findViewById(R.id.settingButton);
        if(settingButton != null){
            settingButton.setOnClickListener(v -> {
                Intent intent = new Intent(
                        requireContext(), SettingActivity.class
                );
                startActivity(intent);
            });
        }

        Button followerButton = view.findViewById(R.id.followerButton);
        if(followerButton != null){
            followerButton.setOnClickListener(v -> {
                Intent intent = new Intent(
                        requireContext(), FollowersActivity.class
                );
                startActivity(intent);
            });
        }
        // Inflate the layout for this fragment
        return view;
    }
}