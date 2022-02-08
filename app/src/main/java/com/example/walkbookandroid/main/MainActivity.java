package com.example.walkbookandroid.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.walkbookandroid.R;
import com.example.walkbookandroid.auth.AuthActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getHashKey();

        addLocationPermission();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new MapFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.mapItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MapFragment()).commit();
                        return true;
                    case R.id.postsItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new PostsFragment()).commit();
                        return true;
                    case R.id.createPostItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new CreatePostFragment()).commit();
                        return true;
                    case R.id.myWalkbookItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MyWalkbookFragment()).commit();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new PostsFragment()).commit();

                Bundle bundle = new Bundle();
                bundle.putString("query", query);

                PostsFragment postsFragment = new PostsFragment();
                postsFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, postsFragment)
                        .addToBackStack(null)
                        .commit();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                showToast("Search clicked");
                return true;
            case R.id.action_account:
                showToast("Account clicked");
                return true;
            case R.id.action_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("로그아웃하시겠습니까?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, AuthActivity.class);

                        SharedPreferences pref = getSharedPreferences("auth", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();

                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    public void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void getHashKey() {
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

    private void addLocationPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        showToast("허용된 권한 개수: " + data.size());
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        showToast("거부된 권한 개수: " + data.size());
                    }
                })
                .start();
    }
}