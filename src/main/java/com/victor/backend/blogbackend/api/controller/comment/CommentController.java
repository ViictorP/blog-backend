package com.victor.backend.blogbackend.api.controller.comment;

import com.victor.backend.blogbackend.api.model.CommentBody;
import com.victor.backend.blogbackend.api.model.CommentResponseBody;
import com.victor.backend.blogbackend.api.model.EditCommentBody;
import com.victor.backend.blogbackend.api.model.LikeResponseBody;
import com.victor.backend.blogbackend.exception.PostNotFoundException;
import com.victor.backend.blogbackend.exception.UserNotFoundException;
import com.victor.backend.blogbackend.exception.UserPermissionDenied;
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

    @PostMapping("/make")
    public ResponseEntity<CommentResponseBody> makeComment(@Valid @RequestBody CommentBody commentBody, @RequestParam int postId, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            CommentResponseBody comment = commentService.makeComment(commentBody, postId, authorizationHeader);
            return ResponseEntity.ok(comment);
        } catch (PostNotFoundException | UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/show")
    public List<CommentResponseBody> postComments(@RequestParam int postId) {
        return commentService.postComments(postId);
    }

    @PostMapping("/edit")
    public ResponseEntity<CommentResponseBody> editComment(@RequestBody EditCommentBody editCommentBody, HttpServletRequest request, @RequestParam long commentId) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            CommentResponseBody comment = commentService.editComment(editCommentBody, authorizationHeader, commentId);
            return ResponseEntity.ok(comment);
        } catch (UserNotFoundException | PostNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (UserPermissionDenied ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/delete")
    public ResponseEntity deleteComment(HttpServletRequest request, @RequestParam long commentId) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            commentService.deleteComment(authorizationHeader, commentId);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException | PostNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (UserPermissionDenied ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/like")
    public ResponseEntity<LikeResponseBody> likeComment(@RequestParam long commentId, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            LikeResponseBody like = commentService.likeComment(authorizationHeader, commentId);
            return ResponseEntity.ok(like);
        } catch (UserNotFoundException | PostNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
