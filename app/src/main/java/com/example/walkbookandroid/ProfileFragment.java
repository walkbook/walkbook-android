package com.example.walkbookandroid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    MainActivity activity;

    TextView nicknameTextView;
    TextView locationTextView;
    TextView introductionTextView;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
        activity = (MainActivity) container.getContext();

        nicknameTextView = rootView.findViewById(R.id.nicknameTextView);
        locationTextView = rootView.findViewById(R.id.locationTextView);
        introductionTextView = rootView.findViewById(R.id.introductionTextView);

        SharedPreferences pref = activity.getSharedPreferences("auth", Activity.MODE_PRIVATE);

        if ((pref != null) && (pref.contains("token"))) {
            String[] addrStr = pref.getString("location", "").split(" ");
            nicknameTextView.setText(pref.getString("nickname", ""));
            locationTextView.setText(addrStr[0] + " " + addrStr[1]);
            introductionTextView.setText(pref.getString("introduction", ""));
        }

        return rootView;
    }
}