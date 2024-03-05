package com.victor.backend.blogbackend.model.dao;

import com.victor.backend.blogbackend.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface PostDAO extends ListCrudRepository<Post, Long> {

    List<Post> OrderByTimeDesc();

    Optional<List<Post>> findByAuthor_UsernameOrderByTimeDesc(String username);

}
