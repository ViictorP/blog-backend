package com.victor.backend.blogbackend.api.model;

public class LikeResponseBody {

    private long likes;

    public LikeResponseBody(long likes) {
        this.likes = likes;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }
}
