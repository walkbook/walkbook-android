package com.example.walkbookandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinFragment extends Fragment {
    LoginActivity activity;
    String[] items = { "성별을 선택하세요", "남성", "여성" };
    Spinner spinner;

    EditText editId;
    EditText editPassword;
    EditText editPassword2;
    EditText editNickname;
    EditText editAge;
    String editSex;
    EditText editAddress;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_join, container, false);
        activity = (LoginActivity) rootView.getContext();

        editId = rootView.findViewById(R.id.join_id_input);
        editPassword = rootView.findViewById(R.id.join_password_input);
        editPassword2 = rootView.findViewById(R.id.join_password_input2);
        editNickname = rootView.findViewById(R.id.join_nickname_input);
        editAge = rootView.findViewById(R.id.join_age_input);
        editAddress = rootView.findViewById(R.id.join_address_input);

        // Spinner
        spinner = rootView.findViewById(R.id.join_sex_input);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.join_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 1 || position == 2) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                    if (position == 1) editSex = "F";
                    else if (position == 2) editSex = "M";
                    else editSex = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Address
//        editAddr = activity.findViewById(R.id.join_address_input);
//        editAddr.setFocusable(false);
//
//        editAddr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        // Join Button
        Button joinButton = rootView.findViewById(R.id.join_button);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editId.getText().toString().equals("") || editPassword.getText().toString().equals("") ||
                        editPassword2.getText().toString().equals("") ||
                        editNickname.getText().toString().equals("") || editSex.equals("") ||
                        editAge.getText().toString().equals("") || editAddress.getText().toString().equals("")) {
                    activity.showToast("모두 입력해야 회원가입할 수 있습니다");
                    return;
                }

                if ( !editPassword.getText().toString().equals(editPassword2.getText().toString()) ) {
                    activity.showToast("패스워드가 서로 다릅니다");
                    return;
                }

                makeRequest();
            }
        });

        // Login Button
        Button loginButton = rootView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog(activity);
            }
        });

        return rootView;
    }

    private void makeRequest() {
        JoinRequest requestBody = new JoinRequest(
                editId.getText().toString(),
                editPassword.getText().toString(),
                editNickname.getText().toString(),
                editSex,
                editAge.getText().toString(),
                editAddress.getText().toString(),
                "안녕하세요. " + editNickname.getText().toString() + "입니다"
        );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://walkbook-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<JoinResponse> call = service.join(requestBody);

        call.enqueue(new Callback<JoinResponse>() {
            @Override
            public void onResponse(Call<JoinResponse> call, Response<JoinResponse> response) {
                if (response.isSuccessful()) {
                    JoinResponse result = response.body();
                    Log.d("LOG_RETROFIT", "Join 성공, 결과 : " + result.getData().getUserId());
                    activity.showToast("회원가입 완료!");

                    // TODO login

                    Intent intent = new Intent(activity, MainActivity.class);
                    startActivity(intent);
                } else {
                    Log.d("LOG_RETROFIT", "Join 실패, 결과 : " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<JoinResponse> call, Throwable t) {
                Log.d("LOG_RETROFIT", "Join 실패, message : " + t.getMessage());
            }
        });
    }

    private void showAlertDialog(LoginActivity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("로그인 화면으로 돌아가겠습니까?");

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.onFragmentChanged(1);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
