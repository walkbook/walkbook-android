package com.example.walkbookandroid;

public class Comment {
    int id;
    int authorId;
    String authorName;
    String content;

    public Comment(int id, int authorId, String authorName, String content) {
        this.id = id;
        this.authorId = authorId;
        this.authorName = authorName;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Comment [id=" + id + ", authorId=" + authorId + ", authorName=" + authorName + ", content=" + content + "]";
    }
}
