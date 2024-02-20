package com.victor.backend.blogbackend.api.controller.auth;

import com.victor.backend.blogbackend.api.model.RegistrationBody;
import com.victor.backend.blogbackend.model.LocalUser;
import com.victor.backend.blogbackend.service.LocalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private LocalUserService localUserService;

    @PostMapping("/register")
    public ResponseEntity<LocalUser> registerUser(@RequestBody RegistrationBody registrationBody) {
        LocalUser user = localUserService.registerUser(registrationBody);
        return ResponseEntity.ok(user);
    }
}
