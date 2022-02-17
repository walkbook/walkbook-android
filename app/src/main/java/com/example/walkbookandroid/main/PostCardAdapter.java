package com.example.walkbookandroid.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkbookandroid.PostCard;
import com.example.walkbookandroid.R;

import java.util.List;

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

        public ItemViewHolder(View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.titleTextView);
            descriptionView = itemView.findViewById(R.id.descriptionTextView);
            authorNameView = itemView.findViewById(R.id.authorTextView);
            likeCountView = itemView.findViewById(R.id.likeCount);
            commentCountView = itemView.findViewById(R.id.commentCount);

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
        }

        public void setItem(PostCard item) {
            postId = item.getPostId();
            authorId = item.getAuthorId();
            titleView.setText(item.getTitle());
            descriptionView.setText(item.getDescription());
            authorNameView.setText(item.getAuthorName());
            likeCountView.setText(Integer.toString(item.getLikeCount()));
            commentCountView.setText(Integer.toString(item.getCommentCount()));
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
