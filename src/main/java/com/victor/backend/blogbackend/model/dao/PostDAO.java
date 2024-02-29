package com.victor.backend.blogbackend.model.dao;

import com.victor.backend.blogbackend.model.Post;
import org.springframework.data.repository.ListCrudRepository;

public interface PostDAO extends ListCrudRepository<Post, Long> {
}
