package com.victor.backend.blogbackend.api.controller.comment;

import com.victor.backend.blogbackend.api.model.CommentBody;
import com.victor.backend.blogbackend.api.model.CommentResponseBody;
import com.victor.backend.blogbackend.exception.PostDontExistsException;
import com.victor.backend.blogbackend.exception.UserDontExistsException;
import com.victor.backend.blogbackend.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/make/{postId}")
    public ResponseEntity<CommentResponseBody> makeComment(@Valid @RequestBody CommentBody commentBody, @PathVariable int postId, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            CommentResponseBody comment = commentService.makeComment(commentBody, postId, authorizationHeader);
            return ResponseEntity.ok(comment);
        } catch (PostDontExistsException | UserDontExistsException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/show/{postId}")
    public List<CommentResponseBody> postComments(@PathVariable int postId) {
        return commentService.postComments(postId);
    }
}
