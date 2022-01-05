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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
                    case R.id.postItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new PostFragment()).commit();
                        return true;
                    case R.id.createItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new CreateFragment()).commit();
                        return true;
                    case R.id.profileItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new ProfileFragment()).commit();
                        return true;
                }
                return false;
            }
        });

//        String[] permissions = {
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//        };
//
//        checkPermissions(permissions);
    }

//    public void checkPermissions(String[] permissions) {
//        ArrayList<String> targetList = new ArrayList<String>();
//
//        for (int i = 0; i < permissions.length; i++) {
//            String curPermission = permissions[i];
//            int permissionCheck = ContextCompat.checkSelfPermission(this, curPermission);
//            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//                showToast(curPermission + " 권한 있음.");
//            } else {
//                showToast(curPermission + " 권한 없음.");
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, curPermission)) {
//                    showToast(curPermission + " 권한 설명 필요함.");
//                } else {
//                    targetList.add(curPermission);
//                }
//            }
//        }
//
//        String[] targets = new String[targetList.size()];
//        targetList.toArray(targets);
//
//        if (targets.length > 0) {
//            ActivityCompat.requestPermissions(this, targets, 101);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 101: {
//                if (grantResults.length > 0 &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    showToast("첫 번째 권한을 사용자가 승인함.");
//                } else {
//                    showToast("첫 번째 권한 거부됨.");
//                }
//
//                return;
//            }
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
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
                        showToast("Log out");
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

    private void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
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