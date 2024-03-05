package com.victor.backend.blogbackend.api.model;

import com.victor.backend.blogbackend.model.Post;

import java.time.LocalDateTime;

public class PostResponseBody {

    private Long id;
    private String title;
    private String content;
    private int likes;
    private String author;
    private LocalDateTime time;
    private boolean edited;

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public PostResponseBody(Post post) {
        id = post.getId();
        title = post.getTitle();
        content = post.getContent();
        likes = post.getLikes();
        author = post.getAuthor().getUsername();
        time = post.getTime();
        edited = post.getEdited();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PostResponseBody() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
