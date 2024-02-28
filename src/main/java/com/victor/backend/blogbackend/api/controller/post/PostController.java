package com.victor.backend.blogbackend.api.controller.post;

import com.victor.backend.blogbackend.api.model.PostBody;
import com.victor.backend.blogbackend.api.model.PostResponseBody;
import com.victor.backend.blogbackend.exception.UserAlreadyExistsException;
import com.victor.backend.blogbackend.model.Post;
import com.victor.backend.blogbackend.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@Valid @RequestBody PostBody postBody, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            Post post = postService.createPost(postBody, authorizationHeader);
            return ResponseEntity.ok(post);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public List<PostResponseBody> allPosts() {
        return postService.allPosts();
    }
}
