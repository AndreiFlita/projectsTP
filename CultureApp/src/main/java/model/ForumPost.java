package model;

import java.time.LocalDateTime;

public class ForumPost {
    private int postId;
    private int topicId;
    private int userId;
    private String continut;
    private LocalDateTime dataPostarii;

    // For display
    private String numeAutor;

    public ForumPost() {
        this.dataPostarii = LocalDateTime.now();
    }

    public ForumPost(int topicId, int userId, String continut) {
        this();
        this.topicId = topicId;
        this.userId = userId;
        this.continut = continut;
    }

    // Getters and Setters
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContinut() {
        return continut;
    }

    public void setContinut(String continut) {
        this.continut = continut;
    }

    public LocalDateTime getDataPostarii() {
        return dataPostarii;
    }

    public void setDataPostarii(LocalDateTime dataPostarii) {
        this.dataPostarii = dataPostarii;
    }

    public String getNumeAutor() {
        return numeAutor;
    }

    public void setNumeAutor(String numeAutor) {
        this.numeAutor = numeAutor;
    }

    @Override
    public String toString() {
        return "ForumPost{" +
                "postId=" + postId +
                ", topicId=" + topicId +
                ", userId=" + userId +
                ", numeAutor='" + numeAutor + '\'' +
                '}';
    }
}