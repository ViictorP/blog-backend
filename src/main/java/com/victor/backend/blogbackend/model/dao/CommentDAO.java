package com.victor.backend.blogbackend.model.dao;

import com.victor.backend.blogbackend.model.Comment;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommentDAO extends ListCrudRepository<Comment, Long> {

    Optional<List<Comment>> findByAuthor_UsernameOrderByTimeDesc(String username);


}
