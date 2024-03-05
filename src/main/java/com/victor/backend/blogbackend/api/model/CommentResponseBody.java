package com.victor.backend.blogbackend.api.model;

import com.victor.backend.blogbackend.model.Comment;

import java.time.LocalDateTime;

public class CommentResponseBody {

    private Long id;
    private String content;
    private int likes;
    private LocalDateTime time;
    private String author;

    private boolean edited;

    public CommentResponseBody(Comment comment) {
        id = comment.getId();
        content = comment.getContent();
        likes = comment.getLikes();
        time = comment.getTime();
        author = comment.getAuthor().getUsername();
        edited = comment.getEdited();
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public CommentResponseBody() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
