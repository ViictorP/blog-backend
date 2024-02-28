package com.victor.backend.blogbackend.model.dao;

import com.victor.backend.blogbackend.model.Comment;
import org.springframework.data.repository.ListCrudRepository;

public interface CommentDAO extends ListCrudRepository<Comment, Long> {
}
