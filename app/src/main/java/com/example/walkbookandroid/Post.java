package com.example.walkbookandroid;

public class Post {
    private int postId;
    private int authorId;
    private String authorName;
    private String title;
    private String description;
    private String startLocation;
    private String finishLocation;
    private String tmi;
    private String createdDate;
    private String modifiedDate;
    private boolean liked;
    private int likeCount;
    private int commentCount;
    private Comment[] comments;

    public Post(int postId, int authorId, String authorName, String title, String description,
                String startLocation, String finishLocation, String tmi, String createdDate, String modifiedDate,
                boolean liked, int likeCount, int commentCount, Comment[] comments) {
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
        this.liked = liked;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.comments = comments;
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

    public boolean getLiked() { return liked; }

    public int getLikeCount() { return likeCount; }

    public int getCommentCount() { return commentCount; }

    public Comment[] getComments() { return comments; }
}
