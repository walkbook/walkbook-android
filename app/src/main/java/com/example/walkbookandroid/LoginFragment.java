package com.example.walkbookandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    LoginActivity activity;
    EditText idEditText;
    EditText passwordEditText;
    String authorization;

    static RequestQueue requestQueue;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_login, container, false);
        activity = (LoginActivity) rootView.getContext();

        idEditText = rootView.findViewById(R.id.login_id_input);
        passwordEditText = rootView.findViewById(R.id.login_password_input);

        Button loginButton = rootView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTokenRequest();
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

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(activity);
        }

        return rootView;
    }

    public void getTokenRequest() {
        final String tokenUrl = BuildConfig.BASE_URL + "/api/user/signin";
        Log.i("LOG_VOLLEY", tokenUrl);

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", idEditText.getText().toString());
            jsonBody.put("password", passwordEditText.getText().toString());
            final String requestBody = jsonBody.toString();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, tokenUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("LOG_VOLLEY", response.toString());
                    Gson gson = new Gson();
                    BaseResponse<String> jsonResponse = gson.fromJson(response.toString(), BaseResponse.class);

                    authorization = jsonResponse.data;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                public byte[] getBody() {
                    try {
                        if (requestBody.length() > 0) {
                            return requestBody.getBytes("utf-8");
                        } else {
                            return null;
                        }
                    } catch (UnsupportedEncodingException uee) {
                        return null;
                    }
                }
            };

            request.setShouldCache(false);
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        activity.showToast("요청 보냄!");
    }
}
