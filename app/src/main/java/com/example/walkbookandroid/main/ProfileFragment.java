package com.example.walkbookandroid.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.walkbookandroid.R;
import com.example.walkbookandroid.UserRetrofitService;
import com.example.walkbookandroid.auth.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        if (getArguments() == null) {   // My profile
            if ((pref != null) && (pref.contains("token"))) {
                String[] addrStr = pref.getString("location", "").split(" ");
                nicknameTextView.setText(pref.getString("nickname", ""));
                locationTextView.setText(addrStr[0] + " " + addrStr[1]);
                introductionTextView.setText(pref.getString("introduction", ""));
            }
        } else {    // Other's profile
            int userId = getArguments().getInt("userId");
            getUserInfo(userId);
        }

        return rootView;
    }

    private void getUserInfo(int userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://walkbook-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRetrofitService service = retrofit.create(UserRetrofitService.class);

        Call<UserResponse> call = service.getUser(Integer.toString(userId));

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse result = response.body();

                    if (result == null) {
                        activity.showToast("서버와의 통신에 문제가 있습니다");
                        return;
                    }

                    Log.d("LOG_RETROFIT", "Get user info 성공, userId : " + result.getData().getUserId());

                    nicknameTextView.setText(result.getData().getNickname());
                    locationTextView.setText(result.getData().getLocation());
                    introductionTextView.setText(result.getData().getIntroduction());
                } else {
                    activity.showToast("서버와의 통신에 문제가 있습니다");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("LOG_RETROFIT", "Get user 실패, message : " + t.getMessage());
            }
        });
    }
}