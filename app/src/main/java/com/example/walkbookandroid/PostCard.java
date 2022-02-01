package com.example.walkbookandroid;

public class PostCard {
    int postId;
    String title;
    String description;
    int authorId;
    String authorName;

    public PostCard(int postId, String title, String description, int authorId, String authorName) {
        this.postId = postId;
        this.title = title;
        this.description = description;
        this.authorId = authorId;
        this.authorName = authorName;
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

    @Override
    public String toString() {
        return "PostCard [id=" + postId + ", title=" + title + ", description=" + description + ", authorId=" + authorId + ", authorName=" + authorName + "]";
    }

}
