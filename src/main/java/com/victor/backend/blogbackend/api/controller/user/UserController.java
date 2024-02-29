package com.victor.backend.blogbackend.api.controller.user;

import com.victor.backend.blogbackend.api.model.UserBody;
import com.victor.backend.blogbackend.exception.UserDontExistsException;
import com.victor.backend.blogbackend.service.LocalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private LocalUserService localUserService;

    @GetMapping
    public ResponseEntity<UserBody> findUser(@RequestParam String username) {
        try {
            UserBody user = localUserService.findUser(username);
            return ResponseEntity.ok(user);
        } catch (UserDontExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
