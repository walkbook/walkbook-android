package com.example.walkbookandroid.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkbookandroid.PostCard;
import com.example.walkbookandroid.R;
import com.example.walkbookandroid.UserRetrofitService;
import com.example.walkbookandroid.auth.UserResponse;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {
    private MainActivity activity;
    private SharedPreferences pref;
    private PostCardAdapter adapter;
    private ArrayList<PostCard> postCards = new ArrayList<>();;
    private RecyclerView recyclerView;

    private int userId;
    private TextView nickname;
    private TextView location;
    private TextView introduction;
    private Button editProfileButton;
    private Button myPostsButton;
    private Button likedPostsButton;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
        activity = (MainActivity) container.getContext();

        nickname = rootView.findViewById(R.id.nicknameTextView);
        location = rootView.findViewById(R.id.locationTextView);
        introduction = rootView.findViewById(R.id.introductionTextView);

        editProfileButton = rootView.findViewById(R.id.editProfileButton);
        myPostsButton = rootView.findViewById(R.id.myPostsButton);
        likedPostsButton = rootView.findViewById(R.id.likedPostsButton);

        recyclerView = rootView.findViewById(R.id.postsRecyclerView);

        pref = activity.getSharedPreferences("auth", Activity.MODE_PRIVATE);

        if (isMyProfile()) {
            userId = pref.getInt("userId", 0);

            String[] addrStr = pref.getString("location", "").split(" ");
            setNicknameLocationIntroduction(
                    pref.getString("nickname", ""),
                    addrStr[0] + " " + addrStr[1],
                    pref.getString("introduction", "")
            );

            addListenerToEditProfile();
        } else {
            userId = getArguments().getInt("userId");
            getUserInfo(userId);

            editProfileButton.setVisibility(View.GONE);
        }

        addListenerToMyPostsButton();
        addListenerToLikedPostsButton();
        initPostsAdaptor();
        getMyPosts();

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

    private void addListenerToMyPostsButton() {
        myPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyPosts();

                likedPostsButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                myPostsButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
            }
        });
    }

    private void addListenerToLikedPostsButton() {
        likedPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLikedPosts();

                myPostsButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                likedPostsButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
            }
        });
    }

    private void initPostsAdaptor() {
        adapter = new PostCardAdapter(postCards);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getMyPosts() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://walkbook-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRetrofitService service = retrofit.create(UserRetrofitService.class);

        Call<PostCardsResponse> call = service.getUserMyPosts(pref.getString("token", ""), userId);

        call.enqueue(new Callback<PostCardsResponse>() {
            @Override
            public void onResponse(Call<PostCardsResponse> call, Response<PostCardsResponse> response) {
                if (response.isSuccessful()) {
                    PostCardsResponse result = response.body();

                    if (result == null) {
                        activity.showToast("서버와의 통신에 문제가 있습니다");
                        return;
                    }

                    Log.d("LOG_RETROFIT", "Get my posts 성공 : " + result.getData());

                    postCards.clear();
                    postCards.addAll(Arrays.asList(result.getData()));
                    adapter.notifyDataSetChanged();
                } else {
                    activity.showToast("서버와의 통신에 문제가 있습니다");
                }
            }

            @Override
            public void onFailure(Call<PostCardsResponse> call, Throwable t) {
                Log.e("LOG_RETROFIT", "Get user my posts 실패, message : " + t.getMessage());
            }
        });
    }

    private void getLikedPosts() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://walkbook-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRetrofitService service = retrofit.create(UserRetrofitService.class);

        Call<PostCardsResponse> call = service.getUserLikedPosts(pref.getString("token", ""), userId);

        call.enqueue(new Callback<PostCardsResponse>() {
            @Override
            public void onResponse(Call<PostCardsResponse> call, Response<PostCardsResponse> response) {
                if (response.isSuccessful()) {
                    PostCardsResponse result = response.body();

                    if (result == null) {
                        activity.showToast("서버와의 통신에 문제가 있습니다");
                        return;
                    }

                    Log.d("LOG_RETROFIT", "Get liked posts 성공 : " + result.getData());

                    postCards.clear();
                    postCards.addAll(Arrays.asList(result.getData()));
                    adapter.notifyDataSetChanged();
                } else {
                    activity.showToast("서버와의 통신에 문제가 있습니다");
                }
            }

            @Override
            public void onFailure(Call<PostCardsResponse> call, Throwable t) {
                Log.e("LOG_RETROFIT", "Get user liked posts 실패, message : " + t.getMessage());
            }
        });
    }
}