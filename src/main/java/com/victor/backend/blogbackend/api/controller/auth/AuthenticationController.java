package com.victor.backend.blogbackend.api.controller.auth;

import com.victor.backend.blogbackend.api.model.LoginBody;
import com.victor.backend.blogbackend.api.model.LoginResponseBody;
import com.victor.backend.blogbackend.api.model.RegistrationBody;
import com.victor.backend.blogbackend.exception.UserAlreadyExistsException;
import com.victor.backend.blogbackend.model.LocalUser;
import com.victor.backend.blogbackend.service.LocalUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private LocalUserService localUserService;

    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
        try {
            localUserService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseBody> loginUser(@Valid @RequestBody LoginBody loginBody) {
        String jwt = localUserService.loginUser(loginBody);
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            LoginResponseBody loginResponseBody = new LoginResponseBody();
            loginResponseBody.setJwt(jwt);
            return ResponseEntity.ok(loginResponseBody);
        }
    }

    @GetMapping("/me")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user) {
        return user;
    }
}
