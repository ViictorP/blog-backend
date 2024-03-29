package com.victor.backend.blogbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "local_user")
public class LocalUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(name = "password", nullable = false, length = 1000)
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 320)
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "biography", length = 200)
    private String biography;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @JsonIgnore
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @OrderBy("id desc")
    private List<VerificationToken> verificationTokens = new ArrayList<>();

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    @JsonIgnore
    @ManyToMany(mappedBy = "postLikes")
    private Set<Post> userPostsLikes = new LinkedHashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "commentLikes")
    private Set<Comment> commentUserLikes = new LinkedHashSet<>();

    public Set<Comment> getCommentUserLikes() {
        return commentUserLikes;
    }

    public void setCommentUserLikes(Set<Comment> commentUserLikes) {
        this.commentUserLikes = commentUserLikes;
    }

    public Set<Post> getUserPostsLikes() {
        return userPostsLikes;
    }

    public void setUserPostsLikes(Set<Post> userPostsLikes) {
        this.userPostsLikes = userPostsLikes;
    }

    public Boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public List<VerificationToken> getVerificationTokens() {
        return verificationTokens;
    }

    public void setVerificationTokens(List<VerificationToken> verificationTokens) {
        this.verificationTokens = verificationTokens;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalUser user = (LocalUser) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(posts, user.posts) && Objects.equals(comments, user.comments) && Objects.equals(biography, user.biography) && Objects.equals(registrationDate, user.registrationDate) && Objects.equals(verificationTokens, user.verificationTokens) && Objects.equals(emailVerified, user.emailVerified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, posts, comments, biography, registrationDate, verificationTokens, emailVerified);
    }
}