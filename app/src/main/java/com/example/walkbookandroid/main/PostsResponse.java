package com.example.walkbookandroid.main;

import com.example.walkbookandroid.PostCard;

public class PostsResponse {
    PostCard[] content;
    String pageable;
    boolean last;
    int totalElements;
    int totalPages;
    int size;
    int number;
    String sort;
    boolean first;
    int numberOfElements;
    boolean empty;

    public PostCard[] getData() { return content; }
}
