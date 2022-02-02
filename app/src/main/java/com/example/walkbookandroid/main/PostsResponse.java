package com.example.walkbookandroid.main;

import com.example.walkbookandroid.PostCard;

public class PostsResponse {
    PostCard[] content;
    Pageable pageable;
    boolean last;
    int totalElements;
    int totalPages;
    int size;
    int number;
    Sort sort;
    boolean first;
    int numberOfElements;
    boolean empty;

    public PostCard[] getData() { return content; }

    public int getTotalPages() { return totalPages; }

    class Pageable {
        Sort sort;
        int offset;
        int pageNumber;
        int pageSize;
        boolean paged;
        boolean unpaged;
    }

    class Sort {
        boolean empty;
        boolean sorted;
        boolean unsorted;
    }
}
