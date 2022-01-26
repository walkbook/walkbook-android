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

import com.example.walkbookandroid.PostRetrofitService;
import com.example.walkbookandroid.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditFragment extends Fragment {
    MainActivity activity;

    int postId;
    EditText title;
    EditText description;
    EditText start;
    EditText end;
    EditText tmi;

    Button editButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_edit, container, false);
        activity = (MainActivity) container.getContext();

        title = rootView.findViewById(R.id.titleEditText);
        description = rootView.findViewById(R.id.descriptionEditText);
        start = rootView.findViewById(R.id.startEditText);
        end = rootView.findViewById(R.id.endEditText);
        tmi = rootView.findViewById(R.id.tmiEditText);

        editButton = rootView.findViewById(R.id.editButton);

        if (getArguments() != null) {
            postId = getArguments().getInt("postId");
            title.setText(getArguments().getString("title"));
            description.setText(getArguments().getString("description"));
            start.setText(getArguments().getString("startLocation"));
            end.setText(getArguments().getString("finishLocation"));
            tmi.setText(getArguments().getString("tmi"));
        }

        handleEditButton();

        return rootView;
    }

    private void handleEditButton() {
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().toString().equals("") || description.getText().toString().equals("") ||
                        start.getText().toString().equals("") || end.getText().toString().equals("") || tmi.getText().toString().equals("")) {
                    activity.showToast("모두 입력해야 합니다");
                    return;
                }
                makeEditRequest();
            }
        });
    }

    private void makeEditRequest() {
        PostRequest requestBody = new PostRequest(
                title.getText().toString(),
                description.getText().toString(),
                start.getText().toString(),
                end.getText().toString(),
                tmi.getText().toString()
        );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://walkbook-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostRetrofitService service = retrofit.create(PostRetrofitService.class);

        SharedPreferences pref = activity.getSharedPreferences("auth", Activity.MODE_PRIVATE);
        if ((pref == null) || !(pref.contains("token"))) {
            activity.showToast("로그인 상태에 문제가 있습니다");
            return;
        }

        Call<PostResponse> call = service.edit(pref.getString("token", ""), Integer.toString(postId), requestBody);

        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()) {
                    PostResponse result = response.body();

                    if (result == null) {
                        activity.showToast("서버와의 통신에 문제가 있습니다");
                        return;
                    }

                    activity.showToast("산책로가 수정되었습니다");

                    Bundle bundle = new Bundle();
                    bundle.putInt("postId", result.getData().getPostId());
                    bundle.putInt("authorId", result.getData().getAuthorId());
                    bundle.putString("authorName", result.getData().getAuthorName());
                    bundle.putString("title", result.getData().getTitle());
                    bundle.putString("description", result.getData().getDescription());
                    bundle.putString("startLocation", result.getData().getStartLocation());
                    bundle.putString("finishLocation", result.getData().getFinishLocation());
                    bundle.putString("tmi", result.getData().getTmi());

                    PostDetailFragment postDetailFragment = new PostDetailFragment();
                    postDetailFragment.setArguments(bundle);

                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frame, postDetailFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    activity.showToast(response.toString());
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.e("LOG_RETROFIT", "Edit post 실패, message : " + t.getMessage());
            }
        });
    }
}
