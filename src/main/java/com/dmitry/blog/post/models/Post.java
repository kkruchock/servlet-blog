package com.dmitry.blog.post.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Post {

    private UUID id;
    private String title;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID authorId;

    public Post() {
    }

    public Post(String title, String text, UUID authorId) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.text = text;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.authorId = authorId;
    }

    public Post(UUID id, String title, String text, LocalDateTime createdAt, LocalDateTime updatedAt, UUID authorId) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.authorId = authorId;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}