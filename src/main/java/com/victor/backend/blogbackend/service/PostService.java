package com.victor.backend.blogbackend.service;

import com.victor.backend.blogbackend.api.model.PostBody;
import com.victor.backend.blogbackend.api.model.PostResponseBody;
import com.victor.backend.blogbackend.exception.UserDontExistsException;
import com.victor.backend.blogbackend.model.LocalUser;
import com.victor.backend.blogbackend.model.Post;
import com.victor.backend.blogbackend.model.dao.LocalUserDAO;
import com.victor.backend.blogbackend.model.dao.PostDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostDAO postDAO;

    @Autowired
    private LocalUserDAO localUserDAO;

    @Autowired
    private JWTService jwt;

    public Post createPost(PostBody postBody, String token) throws UserDontExistsException {
        String username = jwt.getUsername(token);
        Optional<LocalUser> opUser = localUserDAO.findByUsername(username);
        if (opUser.isEmpty()) {
            throw new UserDontExistsException();
        }

        Post post = new Post();
        post.setTitle(postBody.getTitle());
        post.setContent(postBody.getContent());
        LocalUser user = opUser.get();
        post.setAuthor(user);
        post.setLikes(0);
        post.setTime(LocalDateTime.now());
        return postDAO.save(post);
    }

    public List<PostResponseBody> allPosts() {
        List<Post> postList = postDAO.findAll();
        return postList.stream().map(x -> new PostResponseBody(x)).toList();
    }
}
