package com.example.walkbookandroid.main;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkbookandroid.PostCard;
import com.example.walkbookandroid.PostRetrofitService;
import com.example.walkbookandroid.R;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostsFragment extends Fragment {
    private MainActivity activity;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private PostCardAdapter adapter;

    private ArrayList<PostCard> postCards;
    private String searchQuery = null;
    private boolean isLoading = false;
    private int totalPages = 0;
    private int currentPage = 0;

    private final int PAGE_SIZE = 10;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_posts, container, false);
        activity = (MainActivity) rootView.getContext();
        postCards = new ArrayList<>();

        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        hideProgressBar();

        if (getArguments() != null) {
            searchQuery = getArguments().getString("query");
        }

        makePostsRequestWithPageNumber(0);

        return rootView;
    }

    private void hideProgressBar() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                progressBar.setVisibility(View.GONE);
            }
        },1200);
    }

    private void loadPosts(int pageNumber) {
        postCards.add(null);
        adapter.notifyItemInserted(postCards.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                postCards.remove(postCards.size() - 1);
                int scrollPosition = postCards.size();
                adapter.notifyItemRemoved(scrollPosition);

                if (totalPages > pageNumber) {
                    makePostsRequestWithPageNumber(pageNumber);
                }
            }
        }, 1200);
    }

    private void makePostsRequestWithPageNumber(int pageNumber) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://walkbook-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostRetrofitService service = retrofit.create(PostRetrofitService.class);

        Call<PostsResponse> call;

        if (searchQuery == null) {
            call = service.getPosts(pageNumber, PAGE_SIZE, "createdDate,desc");
        } else {
            call = service.getSearchPosts(searchQuery);
        }

        call.enqueue(new Callback<PostsResponse>() {
            @Override
            public void onResponse(Call<PostsResponse> call, Response<PostsResponse> response) {
                if (response.isSuccessful()) {
                    PostsResponse result = response.body();

                    if (result == null) {
                        activity.showToast("서버와의 통신에 문제가 있습니다");
                        return;
                    }

                    Log.d("LOG_RETROFIT", "Get posts 성공, posts : " + Arrays.toString(result.getData()));

                    totalPages = result.getTotalPages();
                    postCards.addAll(Arrays.asList(result.getData()));
                    currentPage++;

                    if (pageNumber == 0) {
                        initAdaptor();
                        initScrollListener();
                    }

                    adapter.notifyDataSetChanged();
                    isLoading = false;
                } else {
                    activity.showToast("서버와의 통신에 문제가 있습니다");
                }
            }

            @Override
            public void onFailure(Call<PostsResponse> call, Throwable t) {
                Log.e("LOG_RETROFIT", "Get posts 실패, message : " + t.getMessage());
            }
        });
    }

    private void initAdaptor() {
        adapter = new PostCardAdapter(postCards);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == postCards.size() - 1) {
                        loadPosts(currentPage);
                        isLoading = true;
                    }
                }
            }
        });
    }
}
