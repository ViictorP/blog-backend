package com.victor.backend.blogbackend.api.model;

import com.victor.backend.blogbackend.model.Post;

import java.time.LocalDateTime;

public class PostResponseBody {

    private String title;
    private String content;
    private int likes;
    private String author;
    private LocalDateTime time;

    public PostResponseBody() {
    }

    public PostResponseBody(Post post) {
        title = post.getTitle();
        content = post.getContent();
        likes = post.getLikes();
        author = post.getAuthor().getUsername();
        time = post.getTime();
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
