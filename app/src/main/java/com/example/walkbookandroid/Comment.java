package com.example.walkbookandroid;

public class Comment {
    private int commentId;
    private int postId;
    private int authorId;
    private String authorName;
    private String content;
    private String createdDate;
    private String modifiedDate;

    public Comment(int commentId, int postId, int authorId, String authorName, String content, String createdDate, String modifiedDate) {
        this.commentId = commentId;
        this.postId = postId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public int getCommentId() {
        return commentId;
    }

    public int getPostId() { return postId; }

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
        return "Comment [commentId=" + commentId + ", postId=" + postId + ", authorId=" + authorId + ", authorName=" + authorName + ", content=" + content + "]";
    }
}
