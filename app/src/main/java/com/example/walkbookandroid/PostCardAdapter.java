package com.example.walkbookandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkbookandroid.main.PostDetailFragment;

import java.util.ArrayList;

public class PostCardAdapter extends RecyclerView.Adapter<PostCardAdapter.ViewHolder> {
    ArrayList<PostCard> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.postcard, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostCard item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(PostCard item) {
        items.add(item);

    }

    public void setItems(ArrayList<PostCard> items) {
        this.items = items;
    }

    public void setItem(int position, PostCard item) {
        items.set(position, item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        int id;
        int authorId;
        TextView titleView;
        TextView descriptionView;
        TextView authorNameView;

        public ViewHolder(View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.titleTextView);
            descriptionView = itemView.findViewById(R.id.descriptionTextView);
            authorNameView = itemView.findViewById(R.id.authorTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);
                    bundle.putInt("authorId", authorId);

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
            id = item.getId();
            authorId = item.getAuthorId();
            titleView.setText(item.getTitle());
            descriptionView.setText(item.getDescription());
            authorNameView.setText(item.getAuthorName());
        }

    }
}
