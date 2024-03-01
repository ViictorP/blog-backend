package com.victor.backend.blogbackend.api.controller.user;

import com.victor.backend.blogbackend.api.model.CommentResponseBody;
import com.victor.backend.blogbackend.api.model.PostResponseBody;
import com.victor.backend.blogbackend.api.model.UserBody;
import com.victor.backend.blogbackend.exception.UserNotFoundException;
import com.victor.backend.blogbackend.exception.UserDontHaveCommentYetException;
import com.victor.backend.blogbackend.exception.UserDontHavePostYetException;
import com.victor.backend.blogbackend.service.CommentService;
import com.victor.backend.blogbackend.service.LocalUserService;
import com.victor.backend.blogbackend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private LocalUserService localUserService;
    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @GetMapping("/profile")
    public ResponseEntity<UserBody> findUser(@RequestParam String username) {
        try {
            UserBody user = localUserService.findUser(username);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseBody>> findUsersPosts(@RequestParam String username) {
        try {
            List<PostResponseBody> postList = postService.findUsersPosts(username);
            return ResponseEntity.ok(postList);
        } catch (UserDontHavePostYetException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseBody>> findUsersComments(@RequestParam String username) {
        try {
            List<CommentResponseBody> commentList = commentService.findUsersComments(username);
            return ResponseEntity.ok(commentList);
        } catch (UserDontHaveCommentYetException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
