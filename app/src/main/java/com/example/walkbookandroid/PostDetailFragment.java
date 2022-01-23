package com.example.walkbookandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class PostDetailFragment extends Fragment {
    MainActivity activity;

    int id;
    int authorId;
    TextView titleTextView;
    Button authorButton;
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

        if (getArguments() != null) {
            id = getArguments().getInt("authorId");
            authorId = getArguments().getInt("authorId");

            // TODO get post info
        }

        authorButton = rootView.findViewById(R.id.authorButton);
        authorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", authorId);

                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(bundle);

                ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, profileFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }
}
