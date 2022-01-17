package com.example.walkbookandroid;

public class PostCard {
    int id;
    String title;
    String description;
    int authorId;
    String authorName;

    public PostCard(int id, String title, String description, int authorId, String authorName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }
}
