package com.victor.backend.blogbackend.service;

import com.victor.backend.blogbackend.api.model.RegistrationBody;
import com.victor.backend.blogbackend.exception.UserAlreadyExistsException;
import com.victor.backend.blogbackend.model.LocalUser;
import com.victor.backend.blogbackend.model.dao.LocalUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalUserService {

    @Autowired
    private LocalUserDAO localUserDAO;

    @Autowired
    private EncryptionService encryptionService;

    public LocalUser registerUser(RegistrationBody registerBody) throws UserAlreadyExistsException {

        if (localUserDAO.findByEmail(registerBody.getEmail()).isPresent()
                || localUserDAO.findByUsername(registerBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        LocalUser user = new LocalUser();
        user.setUsername(registerBody.getUsername());
        user.setEmail(registerBody.getEmail());
        user.setPassword(encryptionService.encryptPassword(registerBody.getPassword()));
        return localUserDAO.save(user);
    }
}
