package com.example.walkbookandroid.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.walkbookandroid.R;

public class EditFragment extends Fragment {
    MainActivity activity;

    int id;
    EditText titleEditText;
    EditText descriptionEditText;
    EditText startEditText;
    EditText endEditText;
    EditText tmiEditText;

    Button editButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_edit, container, false);
        activity = (MainActivity) container.getContext();

        titleEditText = rootView.findViewById(R.id.titleEditText);
        descriptionEditText = rootView.findViewById(R.id.descriptionEditText);
        startEditText = rootView.findViewById(R.id.startEditText);
        endEditText = rootView.findViewById(R.id.endEditText);
        tmiEditText = rootView.findViewById(R.id.tmiEditText);

        editButton = rootView.findViewById(R.id.editButton);

        if (getArguments() != null) {
            id = getArguments().getInt("postId", 0);
            titleEditText.setText(getArguments().getString("title", ""));
            descriptionEditText.setText(getArguments().getString("description", ""));
            startEditText.setText(getArguments().getString("startLocation", ""));
            endEditText.setText(getArguments().getString("finishLocation", ""));
            tmiEditText.setText(getArguments().getString("tmi", ""));
        }

        handleEditButton();

        return rootView;
    }

    private void handleEditButton() {
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO edit request
            }
        });
    }
}
