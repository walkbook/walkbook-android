package com.example.walkbookandroid.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkbookandroid.BaseResponse;
import com.example.walkbookandroid.Comment;
import com.example.walkbookandroid.PostRetrofitService;
import com.example.walkbookandroid.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostDetailFragment extends Fragment {
    MainActivity activity;
    SharedPreferences pref;
    MapViewContainer mapViewContainer;
    CommentAdapter commentAdapter;
    RecyclerView commentRecyclerView;

    int postId;
    int authorId;
    TextView title;
    Button authorButton;
    TextView description;
    TextView start;
    TextView end;
    TextView tmi;

    Button editButton;
    Button deleteButton;

    ImageView likeButton;
    ImageView unlikeButton;
    EditText comment;
    Button commentButton;

    boolean liked = false;

    ArrayList<Comment> comments = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_post_detail, container, false);
        activity = (MainActivity) container.getContext();

        pref = activity.getSharedPreferences("auth", Activity.MODE_PRIVATE);

        // Map
        mapViewContainer = new MapViewContainer(activity);
        ViewGroup mapViewContainer = rootView.findViewById(R.id.map_view);
        mapViewContainer.addView(this.mapViewContainer.getMapView());

        this.mapViewContainer.startLocationService();

        commentRecyclerView = rootView.findViewById(R.id.commentRecyclerView);

        title = rootView.findViewById(R.id.titleText);
        description = rootView.findViewById(R.id.descriptionText);
        start = rootView.findViewById(R.id.startText);
        end = rootView.findViewById(R.id.endText);
        tmi = rootView.findViewById(R.id.tmiText);

        authorButton = rootView.findViewById(R.id.authorButton);
        editButton = rootView.findViewById(R.id.editButton);
        deleteButton = rootView.findViewById(R.id.deleteButton);

        likeButton = rootView.findViewById(R.id.likeButton);
        unlikeButton = rootView.findViewById(R.id.unlikeButton);
        comment = rootView.findViewById(R.id.commentEditText);
        commentButton = rootView.findViewById(R.id.commentButton);

        if (getArguments() != null) {
            postId = getArguments().getInt("postId");

            if (getArguments().getString("title") == null) {
                makePostRequest();
            } else {
                authorId = getArguments().getInt("authorId");
                authorButton.setText(getArguments().getString("authorName"));
                title.setText(getArguments().getString("title"));
                description.setText(getArguments().getString("description"));
                start.setText(getArguments().getString("startLocation"));
                end.setText(getArguments().getString("finishLocation"));
                tmi.setText(getArguments().getString("tmi"));
            }
        }

        addListenerToAuthorButton();
        addListenerToEditAndDeleteButton();
        addListenerToLikeButton();
        addListenerToCommentButton();

        initCommentAdaptor();

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        ((ViewGroup) mapViewContainer.getMapView().getParent()).removeView(mapViewContainer.getMapView());
    }

    private void initCommentAdaptor() {
        commentAdapter = new CommentAdapter(comments);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        commentRecyclerView.setAdapter(commentAdapter);
        commentRecyclerView.setLayoutManager(layoutManager);
    }

    public void makePostRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://walkbook-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostRetrofitService service = retrofit.create(PostRetrofitService.class);

        Call<PostResponse> call = service.getPost(pref.getString("token", ""), Integer.toString(postId));

        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()) {
                    PostResponse result = response.body();

                    if (result == null) {
                        activity.showToast("???????????? ????????? ????????? ????????????");
                        return;
                    }

                    authorId = result.getData().getAuthorId();
                    authorButton.setText(result.getData().getAuthorName());
                    title.setText(result.getData().getTitle());
                    description.setText(result.getData().getDescription());
                    start.setText(result.getData().getStartLocation());
                    end.setText(result.getData().getFinishLocation());
                    tmi.setText(result.getData().getTmi());

                    if (result.getData().getLiked()) {
                        unlikeButton.setVisibility(View.VISIBLE);
                        likeButton.setVisibility(View.GONE);
                    } else {
                        likeButton.setVisibility(View.VISIBLE);
                        unlikeButton.setVisibility(View.GONE);
                    }

                    List commentsList = Arrays.asList(result.getData().getComments());
                    Collections.reverse(commentsList);

                    comments.addAll(commentsList);
                    commentAdapter.notifyDataSetChanged();

                    addListenerToAuthorButton();
                    addListenerToEditAndDeleteButton();
                    addListenerToLikeButton();
                    addListenerToCommentButton();
                } else {
                    activity.showToast(response.toString());
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.e("LOG_RETROFIT", "Get post ??????, message : " + t.getMessage());
            }
        });
    }

    private void addListenerToAuthorButton() {
        authorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", authorId);

                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(bundle);

                ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, profileFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void addListenerToEditAndDeleteButton() {
        if ((pref != null) && (pref.contains("token"))) {
            if (authorId == pref.getInt("userId", 0)) {
                editButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("postId", postId);
                        bundle.putString("title", title.getText().toString());
                        bundle.putString("description", description.getText().toString());
                        bundle.putString("startLocation", start.getText().toString());
                        bundle.putString("finishLocation", end.getText().toString());
                        bundle.putString("tmi", tmi.getText().toString());

                        EditPostFragment editPostFragment = new EditPostFragment();
                        editPostFragment.setArguments(bundle);

                        ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, editPostFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showAlertDialog();
                    }
                });
            } else {
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
            }
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("?????? ?????????????????????????");

        builder.setPositiveButton("???", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                makeDeleteRequest();
            }
        });

        builder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void makeDeleteRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://walkbook-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostRetrofitService service = retrofit.create(PostRetrofitService.class);

        if ((pref == null) || !(pref.contains("token"))) {
            activity.showToast("????????? ????????? ????????? ????????????");
            return;
        }

        Call<BaseResponse> call = service.deletePost(pref.getString("token", ""), postId);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse result = response.body();

                    if (result == null) {
                        activity.showToast("???????????? ????????? ????????? ????????????");
                        return;
                    }

                    activity.showToast("?????????????????????");

                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frame, new PostsFragment())
                            .addToBackStack(null)
                            .commit();
                } else {
                    activity.showToast(response.toString());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("LOG_RETROFIT", "Delete post ??????, message : " + t.getMessage());
            }
        });
    }

    private void addListenerToLikeButton() {
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unlikeButton.setVisibility(View.VISIBLE);
                likeButton.setVisibility(View.GONE);

                liked = !liked;
                makeLikeRequest();
            }
        });

        unlikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeButton.setVisibility(View.VISIBLE);
                unlikeButton.setVisibility(View.GONE);

                liked = !liked;
                makeLikeRequest();
            }
        });
    }

    private void makeLikeRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://walkbook-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostRetrofitService service = retrofit.create(PostRetrofitService.class);

        if ((pref == null) || !(pref.contains("token"))) {
            activity.showToast("????????? ????????? ????????? ????????????");
            return;
        }

        Call<BaseResponse> call = service.likePost(pref.getString("token", ""), postId);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("LOG_RETROFIT", "Like ??????, postId : " + postId);
                    activity.showToast("????????? ??????!");
                } else {
                    activity.showToast("???????????? ????????? ????????? ????????????.");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("LOG_RETROFIT", "Like ??????, message : " + t.getMessage());
            }
        });
    }

    private void addListenerToCommentButton() {
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = PostDetailFragment.this.comment.getText().toString();
                makeCommentRequest(comment);

                PostDetailFragment.this.comment.setText("");
            }
        });
    }

    private void makeCommentRequest(String comment) {
        CommentRequest requestBody = new CommentRequest(comment);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://walkbook-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostRetrofitService service = retrofit.create(PostRetrofitService.class);

        if ((pref == null) || !(pref.contains("token"))) {
            activity.showToast("????????? ????????? ????????? ????????????");
            return;
        }

        Call<CommentResponse> call = service.createComment(pref.getString("token", ""), postId, requestBody);

        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful()) {
                    CommentResponse result = response.body();

                    Log.d("LOG_RETROFIT", "Create Comment ??????, postId : " + postId + ", comment : " + comment);
                    activity.showToast("????????? ??????????????? ?????????????????????!");

                    comments.add(0, result.getData());
                    commentAdapter.notifyDataSetChanged();
                } else {
                    activity.showToast("???????????? ????????? ????????? ????????????.");
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Log.e("LOG_RETROFIT", "Create comment ??????, message : " + t.getMessage());
            }
        });
    }
}
