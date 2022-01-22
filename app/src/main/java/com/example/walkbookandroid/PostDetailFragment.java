package com.example.walkbookandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PostDetailFragment extends Fragment {
    MainActivity activity;

    TextView titleTextView;
    TextView descriptionTextView;
    TextView startTextView;
    TextView endTextView;
    TextView tmiTextView;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_post_detail, container, false);
        activity = (MainActivity) container.getContext();

        titleTextView = rootView.findViewById(R.id.titleText);
        descriptionTextView = rootView.findViewById(R.id.descriptionText);
        startTextView = rootView.findViewById(R.id.startText);
        endTextView = rootView.findViewById(R.id.endText);
        tmiTextView = rootView.findViewById(R.id.tmiText);

        return rootView;
    }
}
