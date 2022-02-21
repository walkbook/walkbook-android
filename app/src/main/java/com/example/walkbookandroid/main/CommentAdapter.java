package com.example.walkbookandroid.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    ArrayList<Comment> items;

    public CommentAdapter(ArrayList<Comment> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.comment, parent, false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment item = items.get(position);
        holder.setItem(item);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(Comment item) {
        items.add(item);
    }
    public void setItems(ArrayList<Comment> items) {
        this.items = items;
    }
    public void setItem(int position, Comment item) {
        items.set(position, item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        int commentId;
        int postId;
        int authorId;
        Button authorButton;
        TextView content;
        Button deleteCommentButton;

        public ViewHolder(View itemView) {
            super(itemView);
            authorButton = itemView.findViewById(R.id.commentAuthor);
            content = itemView.findViewById(R.id.commentContent);
            deleteCommentButton = itemView.findViewById(R.id.deleteCommentButton);

            authorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("userId", authorId);

                    ProfileFragment profileFragment = new ProfileFragment();
                    profileFragment.setArguments(bundle);

                    FragmentManager manager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.main_frame, profileFragment).addToBackStack(null).commit();
                }
            });

            deleteCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity activity = ((MainActivity) view.getContext());

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

                    Call<BaseResponse> call = service.deleteComment(pref.getString("token", ""), postId, commentId);

                    call.enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.isSuccessful()) {
                                Log.d("LOG_RETROFIT", "Delete Comment 성공, postId : " + postId + ", commentId : " + commentId);

                                itemView.setVisibility(View.GONE);
                            } else {
                                activity.showToast("서버와의 통신에 문제가 있습니다.");
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Log.e("LOG_RETROFIT", "Delete comment 실패, message : " + t.getMessage());
                        }
                    });
                }
            });
        }

        public void setItem(Comment comment) {
            commentId = comment.getCommentId();
            postId = comment.getPostId();
            authorId = comment.getAuthorId();
            authorButton.setText(comment.getAuthorName());
            content.setText(comment.getContent());
        }
    }
}