package com.example.walkbookandroid.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    SharedPreferences pref;

    TextView nickname;
    TextView location;
    TextView introduction;
    Button editProfileButton;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
        activity = (MainActivity) container.getContext();

        nickname = rootView.findViewById(R.id.nicknameTextView);
        location = rootView.findViewById(R.id.locationTextView);
        introduction = rootView.findViewById(R.id.introductionTextView);

        editProfileButton = rootView.findViewById(R.id.editProfileButton);

        pref = activity.getSharedPreferences("auth", Activity.MODE_PRIVATE);

        if (isMyProfile()) {
            String[] addrStr = pref.getString("location", "").split(" ");
            setNicknameLocationIntroduction(
                    pref.getString("nickname", ""),
                    addrStr[0] + " " + addrStr[1],
                    pref.getString("introduction", "")
            );

            addListenerToEditProfile();
        } else {
            int userId = getArguments().getInt("userId");
            getUserInfo(userId);

            editProfileButton.setVisibility(View.GONE);
        }

        return rootView;
    }

    private boolean isMyProfile() {
        return ( (getArguments() == null) && (pref != null) && (pref.contains("token")) )
                || (getArguments() != null) && getArguments().getInt("userId") == pref.getInt("userId", 0);
    }

    private void addListenerToEditProfile() {
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new EditProfileFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void getUserInfo(int userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://walkbook-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRetrofitService service = retrofit.create(UserRetrofitService.class);

        Call<UserResponse> call = service.getUser(userId);

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

                    setNicknameLocationIntroduction(
                            result.getData().getNickname(),
                            result.getData().getLocation(),
                            result.getData().getIntroduction()
                    );
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

    private void setNicknameLocationIntroduction(String nickname, String location, String introduction) {
        this.nickname.setText(nickname);
        this.location.setText(location);
        this.introduction.setText(introduction);
    }
}