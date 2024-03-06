package com.victor.backend.blogbackend.api.model;

import com.victor.backend.blogbackend.model.Post;

import java.time.LocalDateTime;

public class PostResponseBody {

    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime time;
    private long likes;
    private boolean edited;

    public PostResponseBody(Post post) {
        id = post.getId();
        title = post.getTitle();
        content = post.getContent();
        author = post.getAuthor().getUsername();
        time = post.getTime();
        likes = post.getPostLikes().size();
        edited = post.getEdited();
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
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
