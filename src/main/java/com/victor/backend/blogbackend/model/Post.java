package com.victor.backend.blogbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @NotBlank
    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @ManyToOne(optional = false)
    @JoinColumn(name = "locar_user_id", nullable = false)
    private LocalUser author;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @JsonIgnore
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "edited")
    private Boolean edited;

    @ManyToMany
    @JoinTable(name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "local_user_id"))
    private Set<LocalUser> postLikes = new LinkedHashSet<>();

    public Set<LocalUser> getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(Set<LocalUser> postLikes) {
        this.postLikes = postLikes;
    }

    public Boolean getEdited() {
        return edited;
    }

    public void setEdited(Boolean edited) {
        this.edited = edited;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public LocalUser getAuthor() {
        return author;
    }

    public void setAuthor(LocalUser author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Post post = (Post) o;
        return getId() != null && Objects.equals(getId(), post.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}