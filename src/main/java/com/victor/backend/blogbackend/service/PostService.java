package com.victor.backend.blogbackend.service;

import com.victor.backend.blogbackend.api.model.PostBody;
import com.victor.backend.blogbackend.model.LocalUser;
import com.victor.backend.blogbackend.model.Post;
import com.victor.backend.blogbackend.model.dao.LocalUserDAO;
import com.victor.backend.blogbackend.model.dao.PostDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostDAO postDAO;

    @Autowired
    private LocalUserDAO localUserDAO;

    public Post createPost(PostBody postBody) throws Exception {
        Optional<LocalUser> opUser = localUserDAO.findByUsername(postBody.getUser());
        if (opUser.isEmpty()) {
            throw new Exception();
        }

        Post post = new Post();
        post.setTitle(postBody.getTitle());
        post.setContent(postBody.getContent());
        LocalUser user = opUser.get();
        post.setLocarUser(user);
        post.setLikes(0);
        post.setTime(LocalDateTime.now());
        return postDAO.save(post);
    }
}
