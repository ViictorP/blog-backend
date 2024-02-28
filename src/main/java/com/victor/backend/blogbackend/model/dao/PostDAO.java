package com.victor.backend.blogbackend.model.dao;

import com.victor.backend.blogbackend.model.Post;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface PostDAO extends ListCrudRepository<Post, Long> {
}
