package com.example.walkbookandroid.main;

import android.app.Activity;
import android.content.SharedPreferences;
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

public class EditProfileFragment extends Fragment {
    MainActivity activity;
    SharedPreferences pref;

    int userId;
    EditText nickname;
    EditText location;
    EditText introduction;

    Button editButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_edit_profile, container, false);
        activity = (MainActivity) container.getContext();

        nickname = rootView.findViewById(R.id.nicknameEditText);
        location = rootView.findViewById(R.id.locationEditText);
        introduction = rootView.findViewById(R.id.introductionEditText);

        editButton = rootView.findViewById(R.id.profileEditButton);

        pref = activity.getSharedPreferences("auth", Activity.MODE_PRIVATE);

        setDefaultValuesOnEditText(
                pref.getString("nickname", ""),
                pref.getString("location", ""),
                pref.getString("introduction", "")
        );

        handleEditButton();

        return rootView;
    }

    private void setDefaultValuesOnEditText(String nickname, String location, String introduction) {
        this.nickname.setText(nickname);
        this.location.setText(location);
        this.introduction.setText(introduction);
    }

    private void handleEditButton() {
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nickname.getText().toString().equals("") || location.getText().toString().equals("") ||
                        introduction.getText().toString().equals("")) {
                    activity.showToast("모두 입력해야 합니다");
                    return;
                }
                makeEditRequest();
            }
        });
    }

    private void makeEditRequest() {
        String newNickname = nickname.getText().toString();
        String newLocation = location.getText().toString();
        String newIntroduction= introduction.getText().toString();

        // TODO edit profile request

        setSharedPreferences(newNickname, newLocation, newIntroduction);

        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, new ProfileFragment())
                .addToBackStack(null)
                .commit();
    }

    private void setSharedPreferences(String nickname, String location, String introduction) {
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("nickname", nickname);
        editor.putString("location", location);
        editor.putString("introduction", introduction);

        editor.commit();
    }
}
