package com.example.walkbookandroid.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkbookandroid.Comment;
import com.example.walkbookandroid.R;

import java.util.ArrayList;

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
        int id;
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
                    // TODO make delete comment request
                }
            });
        }

        public void setItem(Comment comment) {
            id = comment.getCommentId();
            authorId = comment.getAuthorId();
            authorButton.setText(comment.getAuthorName());
            content.setText(comment.getContent());
        }
    }
}