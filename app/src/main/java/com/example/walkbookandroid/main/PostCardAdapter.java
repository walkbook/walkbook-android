package com.example.walkbookandroid.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkbookandroid.BaseResponse;
import com.example.walkbookandroid.PostCard;
import com.example.walkbookandroid.PostRetrofitService;
import com.example.walkbookandroid.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<PostCard> items;

    public PostCardAdapter(List<PostCard> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.postcard, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void showLoadingView(LoadingViewHolder holder, int position) {

    }

    private void populateItemRows(ItemViewHolder holder, int position) {
        PostCard item = items.get(position);
        holder.setItem(item);
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        int postId;
        int authorId;
        TextView titleView;
        TextView descriptionView;
        TextView authorNameView;
        TextView likeCountView;
        TextView commentCountView;

        ImageView likeButton;
        ImageView unlikeButton;
        boolean liked;

        public ItemViewHolder(View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.titleTextView);
            descriptionView = itemView.findViewById(R.id.descriptionTextView);
            authorNameView = itemView.findViewById(R.id.authorTextView);
            likeCountView = itemView.findViewById(R.id.likeCount);
            commentCountView = itemView.findViewById(R.id.commentCount);

            likeButton = itemView.findViewById(R.id.likeButton);
            unlikeButton = itemView.findViewById(R.id.unlikeButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("postId", postId);

                    PostDetailFragment postDetailFragment = new PostDetailFragment();
                    postDetailFragment.setArguments(bundle);

                    ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frame, postDetailFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeLikeRequest(view);

                    liked = true;
                    handleLikeButton();
                    handleLikeCount();
                }
            });

            unlikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeLikeRequest(view);

                    liked = false;
                    handleLikeButton();
                    handleLikeCount();
                }
            });
        }

        private void makeLikeRequest(View view) {
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

            Call<BaseResponse> call = service.likePost(pref.getString("token", ""), postId);

            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d("LOG_RETROFIT", "Like 성공, postId : " + postId);
                    } else {
                        activity.showToast("서버와의 통신에 문제가 있습니다.");
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Log.e("LOG_RETROFIT", "Like 실패, message : " + t.getMessage());
                }
            });
        }

        public void setItem(PostCard item) {
            postId = item.getPostId();
            authorId = item.getAuthorId();
            titleView.setText(item.getTitle());
            descriptionView.setText(item.getDescription());
            authorNameView.setText(item.getAuthorName());
            likeCountView.setText(Integer.toString(item.getLikeCount()));
            commentCountView.setText(Integer.toString(item.getCommentCount()));
            liked = item.getLiked();

            handleLikeButton();
        }

        private void handleLikeButton() {
            if (liked) {
                likeButton.setVisibility(View.GONE);
                unlikeButton.setVisibility(View.VISIBLE);
            } else {
                unlikeButton.setVisibility(View.GONE);
                likeButton.setVisibility(View.VISIBLE);
            }
        }

        private void handleLikeCount() {
            if (liked) {
                likeCountView.setText(Integer.toString(Integer.parseInt(likeCountView.getText().toString()) + 1));
            } else {
                likeCountView.setText(Integer.toString(Integer.parseInt(likeCountView.getText().toString()) - 1));
            }
        }
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
