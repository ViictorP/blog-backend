package com.victor.backend.blogbackend.model.dao;

import com.victor.backend.blogbackend.model.LocalUser;
import com.victor.backend.blogbackend.model.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface VerificationTokenDAO extends ListCrudRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    void deleteByUser(LocalUser user);
}
