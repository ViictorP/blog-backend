package com.victor.backend.blogbackend.api.controller.auth;

import com.victor.backend.blogbackend.api.model.LoginBody;
import com.victor.backend.blogbackend.api.model.LoginResponseBody;
import com.victor.backend.blogbackend.api.model.PasswordResetBody;
import com.victor.backend.blogbackend.api.model.RegistrationBody;
import com.victor.backend.blogbackend.exception.EmailFailureException;
import com.victor.backend.blogbackend.exception.EmailNotFoundException;
import com.victor.backend.blogbackend.exception.UserAlreadyExistsException;
import com.victor.backend.blogbackend.exception.UserNotVerifiedException;
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
        } catch (EmailFailureException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseBody> loginUser(@Valid @RequestBody LoginBody loginBody) {
        String jwt;
        try {
            jwt = localUserService.loginUser(loginBody);
        } catch (UserNotVerifiedException e) {
            LoginResponseBody loginResponseBody = new LoginResponseBody();
            loginResponseBody.setSuccess(false);
            String reason = "USER_NOT_VERIFIED";
            if (e.isNewEmailSent()) {
                reason += "_EMAIL_RESENT";
            }
            loginResponseBody.setFailureReason(reason);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(loginResponseBody);
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            LoginResponseBody loginResponseBody = new LoginResponseBody();
            loginResponseBody.setJwt(jwt);
            return ResponseEntity.ok(loginResponseBody);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity verifyEmail(@RequestParam String token) {
        if (localUserService.verifyUser(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/forgot")
    public ResponseEntity forgotPassword(@RequestParam String email) {
        try {
            localUserService.forgotPassword(email);
            return ResponseEntity.ok().build();
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/reset")
    public ResponseEntity resetPassword(@Valid @RequestBody PasswordResetBody body) {
        localUserService.resetPassword(body);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user) {
        return user;
    }
}
