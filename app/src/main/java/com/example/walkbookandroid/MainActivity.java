package com.example.walkbookandroid;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getHashKey();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new FragmentMap()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.mapItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentMap()).commit();
                        break;
                    case R.id.postItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentPost()).commit();
                        break;
                    case R.id.createItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentNew()).commit();
                        break;
                    case R.id.profileItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentProfile()).commit();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView textView = (TextView)findViewById(R.id.textView);
        switch (item.getItemId()) {
            case R.id.action_search:
                textView.setText("SEARCH");
                return true ;
            case R.id.action_account:
                textView.setText("ACCOUNT");
                return true ;
            case R.id.action_logout:
                textView.setText("LOGOUT");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Logout");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String message = "You logged out.";
                        textView.setText(message);
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String message = "You're not logging out.";
                        textView.setText(message);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true ;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }

    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

}