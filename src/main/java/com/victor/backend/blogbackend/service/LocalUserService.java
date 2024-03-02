package com.victor.backend.blogbackend.service;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.victor.backend.blogbackend.api.model.*;
import com.victor.backend.blogbackend.exception.*;
import com.victor.backend.blogbackend.model.LocalUser;
import com.victor.backend.blogbackend.model.VerificationToken;
import com.victor.backend.blogbackend.model.dao.LocalUserDAO;
import com.victor.backend.blogbackend.model.dao.VerificationTokenDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LocalUserService {

    @Autowired
    private LocalUserDAO localUserDAO;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationTokenDAO verificationTokenDAO;

    public LocalUser registerUser(RegistrationBody registerBody) throws UserAlreadyExistsException, EmailFailureException {
        if (localUserDAO.findByEmailIgnoreCase(registerBody.getEmail()).isPresent()
                || localUserDAO.findByUsernameIgnoreCase(registerBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        LocalUser user = new LocalUser();
        user.setUsername(registerBody.getUsername());
        user.setEmail(registerBody.getEmail());
        user.setPassword(encryptionService.encryptPassword(registerBody.getPassword()));

        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
        user.setRegistrationDate(LocalDateTime.now());
        return localUserDAO.save(user);
    }

    public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException {
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
                if (user.isEmailVerified()) {
                    return jwtService.generateJWT(user);
                } else {
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();
                    boolean resend = verificationTokens.size() == 0 ||
                            verificationTokens.get(0).getCreatedTimestamp()
                                    .before(new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
                    if (resend) {
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationTokenDAO.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                    }
                    throw new UserNotVerifiedException(resend);
                }
            }
        }
        return null;
    }

    @Transactional
    public boolean verifyUser(String token) {
        Optional<VerificationToken> opToken = verificationTokenDAO.findByToken(token);
        if (opToken.isPresent()) {
            VerificationToken verificationToken = opToken.get();
            LocalUser user = verificationToken.getUser();
            if (!user.isEmailVerified()) {
                user.setEmailVerified(true);
                localUserDAO.save(user);
                verificationTokenDAO.deleteByUser(user);
                return true;
            }
        }
        return false;
    }

    public UserBody findUser(String username) throws UserNotFoundException {
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(username);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        return new UserBody(opUser.get());
    }

    public void forgotPassword(String email) throws EmailFailureException, EmailNotFoundException {
        Optional<LocalUser> opUser = localUserDAO.findByEmailIgnoreCase(email);
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            String token = jwtService.generateResetPassordJWT(user);
            emailService.sendPasswordResetEamil(user, token);
        } else {
            throw new EmailNotFoundException();
        }
    }

    public void resetPassword(PasswordResetBody body) {
        String email = jwtService.getResetPasswordEmail(body.getToken());
        Optional<LocalUser> opUser = localUserDAO.findByEmailIgnoreCase(email);
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            user.setPassword(encryptionService.encryptPassword(body.getPassword()));
            localUserDAO.save(user);
        }
    }

    public ChangeUsernameResponseBody editUsername(ChangeUsernameBody changeUsernameBody, HttpServletRequest request) {
        String username = getUsernameFromToken(request);

        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(username);
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();

            if (encryptionService.verifyPassword(changeUsernameBody.getPassword(), user.getPassword())) {

                user.setUsername(changeUsernameBody.getNewUsername());
                localUserDAO.save(user);
                String jwt = jwtService.generateJWT(user);
                return new ChangeUsernameResponseBody(jwt);
            }
        }
        return null;
    }

    public BiographyBody editBio(BiographyBody bio, HttpServletRequest request) {
        String username = getUsernameFromToken(request);
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(username);
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();

            user.setBiography(bio.getBiography());
            localUserDAO.save(user);
            return new BiographyBody(user.getBiography());
        }
        return null;
    }

    private VerificationToken createVerificationToken(LocalUser user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }

    private String getUsernameFromToken(HttpServletRequest request) {
        String tokenHeader = request.getHeader("Authorization");
        String token = tokenHeader.substring(7);
        return jwtService.getUsername(token);
    }
}
