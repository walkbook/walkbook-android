package com.example.walkbookandroid.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
        if ((pref == null) || !(pref.contains("token"))) {
            activity.showToast("로그인 상태에 문제가 있습니다");
        }

        userId = pref.getInt("userId", 0);

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

        EditProfileRequest requestBody = new EditProfileRequest(newNickname, newLocation, newIntroduction);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://walkbook-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRetrofitService service = retrofit.create(UserRetrofitService.class);

        if ((pref == null) || !(pref.contains("token"))) {
            activity.showToast("로그인 상태에 문제가 있습니다");
            return;
        }

        Call<UserResponse> call = service.editUser(pref.getString("token", ""), userId, requestBody);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse result = response.body();

                    if (result == null) {
                        activity.showToast("서버와의 통신에 문제가 있습니다");
                        return;
                    }

                    activity.showToast("회원정보가 수정되었습니다");

                    setSharedPreferences(newNickname, newLocation, newIntroduction);

                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frame, new ProfileFragment())
                            .addToBackStack(null)
                            .commit();
                } else {
                    activity.showToast(response.toString());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("LOG_RETROFIT", "Edit user 실패, message : " + t.getMessage());
            }
        });
    }

    private void setSharedPreferences(String nickname, String location, String introduction) {
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("nickname", nickname);
        editor.putString("location", location);
        editor.putString("introduction", introduction);

        editor.commit();
    }
}
