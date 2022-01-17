package com.example.walkbookandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PostFragment extends Fragment {
    MainActivity activity;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_post, container, false);
        activity = (MainActivity) rootView.getContext();

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        PostCardAdapter adapter = new PostCardAdapter();

        adapter.addItem(new PostCard(1, "title1", "description1", 1, "author1"));
        adapter.addItem(new PostCard(2, "title2", "description2", 2, "author2"));
        adapter.addItem(new PostCard(3, "title3", "description3", 3, "author3"));

        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
