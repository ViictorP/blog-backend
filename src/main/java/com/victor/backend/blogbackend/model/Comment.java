package com.victor.backend.blogbackend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private LocalUser author;

    @Column(name = "edited")
    private Boolean edited;

    @ManyToMany
    @JoinTable(name = "comment_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "localUsers_id"))
    private Set<LocalUser> commentLikes = new LinkedHashSet<>();

    public Set<LocalUser> getCommentLikes() {
        return commentLikes;
    }

    public void setCommentLikes(Set<LocalUser> commentLikes) {
        this.commentLikes = commentLikes;
    }

    public Boolean getEdited() {
        return edited;
    }

    public void setEdited(Boolean edited) {
        this.edited = edited;
    }

    public LocalUser getAuthor() {
        return author;
    }

    public void setAuthor(LocalUser author) {
        this.author = author;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}