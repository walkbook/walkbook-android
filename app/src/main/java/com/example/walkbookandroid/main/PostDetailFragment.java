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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostDetailFragment extends Fragment {
    MainActivity activity;
    MapViewContainer mapViewContainer;
    CommentAdapter commentAdapter;
    RecyclerView commentRecyclerView;

    int postId;
    int authorId;
    TextView titleTextView;
    Button authorButton;
    TextView descriptionTextView;
    TextView startTextView;
    TextView endTextView;
    TextView tmiTextView;

    Button editButton;
    Button deleteButton;

    Button likeButton;
    Button unlikeButton;
    EditText commentEditText;
    Button commentButton;

    boolean liked = false;

    ArrayList<Comment> comments = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_post_detail, container, false);
        activity = (MainActivity) container.getContext();

        // Map
        mapViewContainer = new MapViewContainer(activity);
        ViewGroup mapViewContainer = rootView.findViewById(R.id.map_view);
        mapViewContainer.addView(this.mapViewContainer.getMapView());

        this.mapViewContainer.startLocationService();

        commentRecyclerView = rootView.findViewById(R.id.commentRecyclerView);

        titleTextView = rootView.findViewById(R.id.titleText);
        descriptionTextView = rootView.findViewById(R.id.descriptionText);
        startTextView = rootView.findViewById(R.id.startText);
        endTextView = rootView.findViewById(R.id.endText);
        tmiTextView = rootView.findViewById(R.id.tmiText);

        authorButton = rootView.findViewById(R.id.authorButton);
        editButton = rootView.findViewById(R.id.editButton);
        deleteButton = rootView.findViewById(R.id.deleteButton);

        likeButton = rootView.findViewById(R.id.likeButton);
        unlikeButton = rootView.findViewById(R.id.unlikeButton);
        commentEditText = rootView.findViewById(R.id.commentEditText);
        commentButton = rootView.findViewById(R.id.commentButton);

        if (getArguments() != null) {
            postId = getArguments().getInt("postId");

            if (getArguments().getString("title") == null) {
                makePostRequest();
            } else {
                authorId = getArguments().getInt("authorId");
                authorButton.setText(getArguments().getString("authorName"));
                titleTextView.setText(getArguments().getString("title"));
                descriptionTextView.setText(getArguments().getString("description"));
                startTextView.setText(getArguments().getString("startLocation"));
                endTextView.setText(getArguments().getString("finishLocation"));
                tmiTextView.setText(getArguments().getString("tmi"));
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

    public void makePostRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://walkbook-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostRetrofitService service = retrofit.create(PostRetrofitService.class);

        Call<PostResponse> call = service.getPost(Integer.toString(postId));

        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()) {
                    PostResponse result = response.body();

                    if (result == null) {
                        activity.showToast("서버와의 통신에 문제가 있습니다");
                        return;
                    }

                    authorId = result.getData().getAuthorId();
                    authorButton.setText(result.getData().getAuthorName());
                    titleTextView.setText(result.getData().getTitle());
                    descriptionTextView.setText(result.getData().getDescription());
                    startTextView.setText(result.getData().getStartLocation());
                    endTextView.setText(result.getData().getFinishLocation());
                    tmiTextView.setText(result.getData().getTmi());

                } else {
                    activity.showToast(response.toString());
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.e("LOG_RETROFIT", "Get post 실패, message : " + t.getMessage());
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
        SharedPreferences pref = activity.getSharedPreferences("auth", Activity.MODE_PRIVATE);

        if ((pref != null) && (pref.contains("token"))) {
            if (authorId == pref.getInt("userId", 0)) {
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("postId", postId);
                        bundle.putString("title", titleTextView.getText().toString());
                        bundle.putString("description", descriptionTextView.getText().toString());
                        bundle.putString("startLocation", startTextView.getText().toString());
                        bundle.putString("finishLocation", endTextView.getText().toString());
                        bundle.putString("tmi", tmiTextView.getText().toString());

                        EditFragment editFragment = new EditFragment();
                        editFragment.setArguments(bundle);

                        ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, editFragment)
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
        builder.setMessage("정말 삭제하시겠습니까?");

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                makeDeleteRequest();
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

    private void makeDeleteRequest() {
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

        Call<BaseResponse> call = service.delete(pref.getString("token", ""), postId);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse result = response.body();

                    if (result == null) {
                        activity.showToast("서버와의 통신에 문제가 있습니다");
                        return;
                    }

                    activity.showToast("삭제되었습니다");

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
                Log.e("LOG_RETROFIT", "Get post 실패, message : " + t.getMessage());
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
        // TODO
        if (liked) activity.showToast("좋아요");
        else activity.showToast("좋아요 취소");
    }

    private void addListenerToCommentButton() {
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = commentEditText.getText().toString();
                makeCommentRequest(comment);

                commentEditText.setText("");

                // TODO show comment right away
            }
        });
    }

    private void makeCommentRequest(String comment) {
        // TODO
        activity.showToast("댓글 \"" + comment + "\"이 등록되었습니다!");
    }

    private void initCommentAdaptor() {
        // TODO delete dummy data
        comments.add(new Comment(1, 1, "user1", "ㅁㄴㅇㄹㄴㅇ"));
        comments.add(new Comment(2, 1, "user1", "ㅁㄴㅇㄹㄴㅇ"));
        comments.add(new Comment(3, 1, "user1", "ㅁㄴㅇㄹㄴㅇ"));

        commentAdapter = new CommentAdapter(comments);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        commentRecyclerView.setAdapter(commentAdapter);
        commentRecyclerView.setLayoutManager(layoutManager);
    }
}
