package com.example.walkbookandroid;

public class PostCard {
    int postId;
    String title;
    String description;
    int authorId;
    String authorName;
    boolean liked;
    int likeCount;
    int commentCount;

    public PostCard(int postId, String title, String description, int authorId, String authorName, boolean liked, int likeCount, int commentCount) {
        this.postId = postId;
        this.title = title;
        this.description = description;
        this.authorId = authorId;
        this.authorName = authorName;
        this.liked = liked;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public int getPostId() {
        return postId;
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

    public boolean getLiked() { return liked; }

    public int getLikeCount() {
        return likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    @Override
    public String toString() {
        return "PostCard [id=" + postId + ", title=" + title + ", description=" + description + ", authorId=" + authorId + ", authorName=" + authorName + "]";
    }

}
