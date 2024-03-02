package com.victor.backend.blogbackend.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BiographyBody {

    @NotNull
    @NotBlank
    private String biography;

    public BiographyBody() {
    }

    public BiographyBody(String biography) {
        this.biography = biography;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
