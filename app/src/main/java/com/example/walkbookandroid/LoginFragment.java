package com.example.walkbookandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

                makeRequest();
            }
        });

        Button joinButton = rootView.findViewById(R.id.join_button);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity activity = (LoginActivity) getActivity();
                activity.onFragmentChanged(0);
            }
        });

        return rootView;
    }

    private void makeRequest() {
        LoginRequest requestBody = new LoginRequest(
                editId.getText().toString(),
                editPassword.getText().toString()
        );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://walkbook-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<LoginResponse> call = service.login(requestBody);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse result = response.body();
                    Log.d("LOG_RETROFIT", "Login 성공, userId : " + result.getData().getUserId() + "\n token : " + result.getToken());
                    activity.showToast(result.getData().getNickname() + " 님, 환영합니다!");

                    SharedPreferences pref = activity.getSharedPreferences("auth", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("token", result.getToken());
                    editor.commit();

                    Intent intent = new Intent(activity, MainActivity.class);
                    startActivity(intent);
                } else {
                    activity.showToast("아이디와 비밀번호를 확인하세요");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LOG_RETROFIT", "Login 실패, message : " + t.getMessage());
            }
        });
    }
}
