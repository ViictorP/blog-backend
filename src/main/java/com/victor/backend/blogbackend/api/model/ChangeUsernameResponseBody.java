package com.victor.backend.blogbackend.api.model;

public class ChangeUsernameResponseBody {

    private String newToken;

    public ChangeUsernameResponseBody(String newToken) {
        this.newToken = newToken;
    }

    public String getNewToken() {
        return newToken;
    }

    public void setNewToken(String newToken) {
        this.newToken = newToken;
    }
}
