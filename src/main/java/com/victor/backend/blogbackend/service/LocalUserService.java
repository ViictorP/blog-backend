package com.victor.backend.blogbackend.service;

import com.victor.backend.blogbackend.api.model.LoginBody;
import com.victor.backend.blogbackend.api.model.RegistrationBody;
import com.victor.backend.blogbackend.api.model.UserBody;
import com.victor.backend.blogbackend.exception.UserAlreadyExistsException;
import com.victor.backend.blogbackend.exception.UserDontExistsException;
import com.victor.backend.blogbackend.model.LocalUser;
import com.victor.backend.blogbackend.model.dao.LocalUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LocalUserService {

    @Autowired
    private LocalUserDAO localUserDAO;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private JWTService jwtService;

    public LocalUser registerUser(RegistrationBody registerBody) throws UserAlreadyExistsException {
        if (localUserDAO.findByEmail(registerBody.getEmail()).isPresent()
                || localUserDAO.findByUsername(registerBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        LocalUser user = new LocalUser();
        user.setUsername(registerBody.getUsername());
        user.setEmail(registerBody.getEmail());
        user.setPassword(encryptionService.encryptPassword(registerBody.getPassword()));
        user.setRegistrationDate(LocalDateTime.now());
        return localUserDAO.save(user);
    }

    public String loginUser(LoginBody loginBody) {
        Optional<LocalUser> opUser = localUserDAO.findByUsername(loginBody.getUsername());
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
                return jwtService.generateJWT(user);
            }
        }
        return null;
    }

    public UserBody findUser(String username) throws UserDontExistsException {
        Optional<LocalUser> opUser = localUserDAO.findByUsername(username);
        if (opUser.isEmpty()) {
            throw new UserDontExistsException();
        }
        return new UserBody(opUser.get());
    }
}
