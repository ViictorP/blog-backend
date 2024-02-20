package com.victor.backend.blogbackend.service;

import com.victor.backend.blogbackend.api.model.RegistrationBody;
import com.victor.backend.blogbackend.model.LocalUser;
import com.victor.backend.blogbackend.model.dao.LocalUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalUserService {

    @Autowired
    private LocalUserDAO localUserDAO;

    public LocalUser registerUser(RegistrationBody registerBody) {
        LocalUser user = new LocalUser();
        user.setUsername(registerBody.getUsername());
        user.setEmail(registerBody.getEmail());
        user.setPassword(registerBody.getPassword());
        return localUserDAO.save(user);
    }
}
