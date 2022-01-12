package com.example.walkbookandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {
    LoginActivity activity;
    EditText editId;
    EditText editPassword;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_login, container, false);
        activity = (LoginActivity) rootView.getContext();

        editId = rootView.findViewById(R.id.login_id_input);
        editPassword = rootView.findViewById(R.id.login_password_input);

        Button loginButton = rootView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editId.getText().toString().equals("")) {
                    activity.showToast("아이디를 입력하세요");
                    return;
                }

                if (editPassword.getText().toString().equals("")) {
                    activity.showToast("비밀번호를 입력하세요");
                    return;
                }

                activity.makeLoginRequest(editId.getText().toString(), editPassword.getText().toString());
            }
        });

        Button joinButton = rootView.findViewById(R.id.join_button);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity activity = (LoginActivity) getActivity();
                if (activity == null) {
                    Log.e("Login", "failed to load LoginActivity");
                    return;
                }

                activity.onFragmentChanged(0);
            }
        });

        return rootView;
    }
}
