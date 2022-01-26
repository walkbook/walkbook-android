package com.example.walkbookandroid;

public class Post {
    int postId;
    int authorId;
    String authorName;
    String title;
    String description;
    String startLocation;
    String finishLocation;
    String tmi;
    String createdDate;
    String modifiedDate;

    public Post(int postId, int authorId, String authorName, String title, String description,
                String startLocation, String finishLocation, String tmi, String createdDate, String modifiedDate) {
        this.postId = postId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.title = title;
        this.description = description;
        this.startLocation = startLocation;
        this.finishLocation = finishLocation;
        this.tmi = tmi;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public int getPostId() {
        return postId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public String getFinishLocation() {
        return finishLocation;
    }

    public String getTmi() {
        return tmi;
    }

}
