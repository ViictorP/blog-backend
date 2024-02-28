package com.victor.backend.blogbackend.service;

import com.victor.backend.blogbackend.api.model.CommentBody;
import com.victor.backend.blogbackend.api.model.CommentResponseBody;
import com.victor.backend.blogbackend.model.Comment;
import com.victor.backend.blogbackend.model.LocalUser;
import com.victor.backend.blogbackend.model.Post;
import com.victor.backend.blogbackend.model.dao.CommentDAO;
import com.victor.backend.blogbackend.model.dao.LocalUserDAO;
import com.victor.backend.blogbackend.model.dao.PostDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private LocalUserDAO localUserDAO;

    @Autowired
    private JWTService jwt;

    @Autowired
    private PostDAO postDAO;

    public CommentResponseBody makeComment(CommentBody commentBody, int postId, String token) throws Exception {
        String username = jwt.getUsername(token);
        Optional<LocalUser> opUser = localUserDAO.findByUsername(username);
        Optional<Post> opPost = postDAO.findById((long) postId);
        if (opUser.isEmpty() || opPost.isEmpty()) {
            throw new Exception();
        }

        LocalUser user = opUser.get();
        Post post = opPost.get();
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setPost(post);
        comment.setContent(commentBody.getContent());
        comment.setTime(LocalDateTime.now());
        comment.setLikes(0);
        return new CommentResponseBody(commentDAO.save(comment));
    }
}
