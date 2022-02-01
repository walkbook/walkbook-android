package com.example.walkbookandroid.main;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private RecyclerView recyclerView;
    private PostCardAdapter adapter;
    private ArrayList<PostCard> items = new ArrayList<>();

    private boolean isLoading = false;

    private final int PAGE_SIZE = 10;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_posts, container, false);
        activity = (MainActivity) rootView.getContext();

        recyclerView = rootView.findViewById(R.id.recyclerView);

        makePostsRequestWithPageNumber(0);
        initAdaptor();
        initScrollListener();

        return rootView;
    }

    private void initAdaptor() {
        adapter = new PostCardAdapter(items);
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
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == items.size() - 1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        items.add(null);
        adapter.notifyItemInserted(items.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                items.remove(items.size() - 1);
                int scrollPosition = items.size();
                adapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize < nextLimit) {
                    items.add(new PostCard(currentSize+1, "title"+(currentSize+1), "description"+(currentSize+1), 1, "author1"));
                    currentSize++;
                }

                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    private void makePostsRequestWithPageNumber(int pageNumber) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://walkbook-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostRetrofitService service = retrofit.create(PostRetrofitService.class);

        Call<PostsResponse> call = service.getPosts(pageNumber, PAGE_SIZE, "createdDate");

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
}
