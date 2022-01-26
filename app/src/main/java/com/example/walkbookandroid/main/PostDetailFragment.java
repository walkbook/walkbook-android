package com.example.walkbookandroid.main;

import android.app.Activity;
import android.content.SharedPreferences;
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

import com.example.walkbookandroid.R;

public class PostDetailFragment extends Fragment {
    MainActivity activity;
    MapViewContainer mapViewContainer;

    int id;
    int authorId;
    TextView titleTextView;
    Button authorButton;
    TextView descriptionTextView;
    TextView startTextView;
    TextView endTextView;
    TextView tmiTextView;

    Button editButton;
    Button deleteButton;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_post_detail, container, false);
        activity = (MainActivity) container.getContext();

        // Map
        mapViewContainer = new MapViewContainer(activity);
        ViewGroup mapViewContainer = rootView.findViewById(R.id.map_view);
        mapViewContainer.addView(this.mapViewContainer.getMapView());

        this.mapViewContainer.startLocationService();

        titleTextView = rootView.findViewById(R.id.titleText);
        descriptionTextView = rootView.findViewById(R.id.descriptionText);
        startTextView = rootView.findViewById(R.id.startText);
        endTextView = rootView.findViewById(R.id.endText);
        tmiTextView = rootView.findViewById(R.id.tmiText);

        authorButton = rootView.findViewById(R.id.authorButton);
        editButton = rootView.findViewById(R.id.editButton);
        deleteButton = rootView.findViewById(R.id.deleteButton);

        if (getArguments() != null) {
            id = getArguments().getInt("authorId");
            authorId = getArguments().getInt("authorId");

            // TODO get post info
        }

        handleAuthorButton();
        handleEditDeleteButton();

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        ((ViewGroup) mapViewContainer.getMapView().getParent()).removeView(mapViewContainer.getMapView());
    }

    private void handleAuthorButton() {
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
    }

    private void handleEditDeleteButton() {
        SharedPreferences pref = activity.getSharedPreferences("auth", Activity.MODE_PRIVATE);

        if ((pref != null) && (pref.contains("token"))) {
            if (authorId == pref.getInt("userId", 0)) {
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("postId", id);
                        bundle.putString("title", titleTextView.getText().toString());
                        bundle.putString("description", descriptionTextView.getText().toString());
                        bundle.putString("startLocation", startTextView.getText().toString());
                        bundle.putString("finishLocation", endTextView.getText().toString());
                        bundle.putString("tmi", tmiTextView.getText().toString());

                        EditFragment editFragment = new EditFragment();
                        editFragment.setArguments(bundle);

                        ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, editFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO delete request
                    }
                });
            } else {
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
            }
        }
    }
}
