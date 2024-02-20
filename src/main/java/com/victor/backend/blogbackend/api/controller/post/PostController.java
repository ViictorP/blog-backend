package com.victor.backend.blogbackend.api.controller.post;

import com.victor.backend.blogbackend.api.model.PostBody;
import com.victor.backend.blogbackend.model.Post;
import com.victor.backend.blogbackend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostBody postBody) {
        Post post = postService.createPost(postBody);
        return ResponseEntity.ok(post);
    }
}
