package com.victor.backend.blogbackend.api.controller.post;

import com.victor.backend.blogbackend.api.model.EditPostBody;
import com.victor.backend.blogbackend.api.model.PostBody;
import com.victor.backend.blogbackend.api.model.PostResponseBody;
import com.victor.backend.blogbackend.exception.PostNotFoundException;
import com.victor.backend.blogbackend.exception.UserNotFoundException;
import com.victor.backend.blogbackend.exception.UserPermissionDenied;
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
    public ResponseEntity<PostResponseBody> createPost(@Valid @RequestBody PostBody postBody, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            PostResponseBody post = postService.createPost(postBody, authorizationHeader);
            return ResponseEntity.ok(post);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/edit/{postId}")
    public ResponseEntity<PostResponseBody> editPost(@RequestBody EditPostBody editPostBody, HttpServletRequest request, @PathVariable long postId) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            PostResponseBody post = postService.editPost(editPostBody, authorizationHeader, postId);
            return ResponseEntity.ok(post);
        } catch (UserNotFoundException | PostNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (UserPermissionDenied ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/delete/{postId}")
    public ResponseEntity deletePost(HttpServletRequest request, @PathVariable long postId) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            postService.deletePost(authorizationHeader, postId);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException | PostNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (UserPermissionDenied ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/all")
    public List<PostResponseBody> allPosts() {
        return postService.allPosts();
    }
}
