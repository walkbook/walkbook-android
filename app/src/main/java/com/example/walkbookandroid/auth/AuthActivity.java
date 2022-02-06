package com.example.walkbookandroid.auth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.walkbookandroid.UserRetrofitService;
import com.example.walkbookandroid.main.MainActivity;
import com.example.walkbookandroid.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthActivity extends AppCompatActivity {
    public ActivityResultLauncher<Intent> resultLauncher;

    LoginFragment loginFragment;
    JoinFragment joinFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginFragment = new LoginFragment();
        joinFragment = new JoinFragment();

        onFragmentChanged(1);

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK){
                            Intent intent = result.getData();
                            if (intent != null) {
                                String data = intent.getExtras().getString("data");
                                if (data != null) {
                                    data = data.split(", ")[1];
                                    data = data.split(" \\(")[0];
                                    joinFragment.address.setText(data);
                                }
                            }
                        }
                    }
                });
    }

    public void onFragmentChanged(int index) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, joinFragment).commit();
        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginFragment).commit();
        }
    }

    public void makeLoginRequest(String id, String password) {
        LoginRequest requestBody = new LoginRequest(id, password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://walkbook-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRetrofitService service = retrofit.create(UserRetrofitService.class);

        Call<LoginResponse> call = service.login(requestBody);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse result = response.body();
                    Log.d("LOG_RETROFIT", "Login 성공, userId : " + result.getData().getUserId() + "\n token : " + result.getToken());
                    showToast(result.getData().getNickname() + " 님, 환영합니다!");

                    SharedPreferences pref = getSharedPreferences("auth", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("token", result.getToken());
                    editor.putInt("userId", result.getData().getUserId());
                    editor.putString("username", result.getData().getUsername());
                    editor.putString("nickname", result.getData().getNickname());
                    editor.putString("gender", result.getData().getGender());
                    editor.putInt("age", result.getData().getAge());
                    editor.putString("location", result.getData().getLocation());
                    editor.putString("introduction", result.getData().getIntroduction());
                    editor.commit();

                    Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    showToast("아이디와 비밀번호를 확인하세요");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LOG_RETROFIT", "Login 실패, message : " + t.getMessage());
            }
        });
    }

    public void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
