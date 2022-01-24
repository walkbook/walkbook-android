package com.example.walkbookandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CreateFragment extends Fragment {
    MainActivity activity;
    EditText editTitle;
    EditText editDescription;
    EditText editStart;
    EditText editEnd;
    EditText editTmi;
    Button createButton;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_create, container, false);
        activity = (MainActivity) container.getContext();

        editTitle = rootView.findViewById(R.id.titleEditText);
        editDescription = rootView.findViewById(R.id.descriptionEditText);
        editStart = rootView.findViewById(R.id.startEditText);
        editEnd = rootView.findViewById(R.id.endEditText);
        editTmi = rootView.findViewById(R.id.tmiEditText);

        createButton = rootView.findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequest();
            }
        });

        return rootView;
    }

    private void makeRequest() {
        // TODO create
        activity.showToast("createButton clicked");
    }
}