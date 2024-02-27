package com.victor.backend.blogbackend.api.model;

public class LoginResponseBody {

    private String jwt;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
