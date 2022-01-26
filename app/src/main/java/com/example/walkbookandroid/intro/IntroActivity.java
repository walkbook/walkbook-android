package com.example.walkbookandroid.intro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.walkbookandroid.auth.AuthActivity;
import com.example.walkbookandroid.main.MainActivity;
import com.example.walkbookandroid.R;

public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent;

                SharedPreferences pref = getSharedPreferences("auth", Activity.MODE_PRIVATE);
                if ((pref != null) && (pref.contains("token"))) {
                    intent = new Intent(IntroActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(IntroActivity.this, AuthActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
