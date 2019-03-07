package org.tomlang.livechat.service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tomlang.livechat.entities.Token;
import org.tomlang.livechat.entities.User;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.repositories.TokenRepository;
import org.tomlang.livechat.util.PasswordEncoder;
import org.tomlang.livechat.util.TokenProvider;

@Service
public class LoginService {
    @Autowired
    private UserService userService;
    @Autowired
    TokenProvider tokenProvider;

    @Value("${app.jwt.ttl}")
    private String jwtTTL;

    @Value("${app.jwt.refresh.ttl}")
    private String refreshTtl;

    @Autowired
    TokenRepository tokenRepository;

    Logger logger = LoggerFactory.getLogger(LoginService.class);

    public Token generateToken(String email, String password) throws LiveChatException {

        logger.info("Attemting login.");
        Token token = new Token();
        User user = userService.getUserByEmail(email);

        if (null != user) {
            logger.info("Found user with given email: " + email);
            String salt = user.getPasswordSalt();
            String encodedIncomingPassword = PasswordEncoder.encodePassword(password, salt);
            if (!encodedIncomingPassword.equals(user.getPassword())) {
                logger.error("Failed to match password....");
                // throw custom exception with message "Invalid credentials"
                throw new LiveChatException("Invalid credentials", HttpStatus.UNAUTHORIZED);
            }

            if (!user.isHasConfirmedEmail()) {
                logger.error("User is not active...email is not confirmed");
                // throw custom exception with message "User is inactive. Please confirm email"
                throw new LiveChatException("User is inactive. Please confirm email", HttpStatus.UNAUTHORIZED);
            }
            logger.info("Password matched and user is active");
            logger.info("Creating access token");
            String tokenString = tokenProvider.createAccessToken(user);

            token.setToken(tokenString);
            token.setUserId(user.getId());
            token.setTtl(System.currentTimeMillis() + Long.parseLong(jwtTTL));

            logger.info("Saving user refresh token.");
            Token refreshToken = new Token();
            refreshToken.setToken(UUID.randomUUID()
                .toString()
                .replaceAll("-", ""));
            refreshToken.setUserId(user.getId());
            refreshToken.setTtl(System.currentTimeMillis() + Long.parseLong(refreshTtl));
            tokenRepository.save(refreshToken);
            user.setLastLoggedIn(new Date(Instant.now()
                .getEpochSecond()));
            logger.info("Updating user with last logged in time");
            userService.updateUser(user);
            token.setRefreshToken(refreshToken.getToken());
        } else {
            throw new LiveChatException("User not found", HttpStatus.UNAUTHORIZED);
        }
        return token;
    }

    public Token refreshAccessToken(String refreshToken) throws LiveChatException {

        logger.info("Attemting login.");
        Token existingRefreshToken = tokenRepository.findByToken(refreshToken);
        if (System.currentTimeMillis() > existingRefreshToken.getTtl()) {
            throw new LiveChatException("Refresh Token Expired", HttpStatus.UNAUTHORIZED);
        }
        User user = userService.getUserbyId(existingRefreshToken.getUserId())
            .get();
        Token token = new Token();
        if (null != user) {
            logger.info("Found user with given email: " + user.getEmail());
            logger.info("Password matched and user is active");
            logger.info("Creating access token");
            String tokenString = tokenProvider.createAccessToken(user);
            
            token.setToken(tokenString);
            token.setUserId(user.getId());
            token.setTtl(System.currentTimeMillis() + Long.parseLong(jwtTTL));

            logger.info("Saving user refresh token.");
            
            existingRefreshToken.setToken(UUID.randomUUID()
                .toString()
                .replaceAll("-", ""));
            
            existingRefreshToken.setTtl(System.currentTimeMillis() + Long.parseLong(refreshTtl));
            tokenRepository.save(existingRefreshToken);
            user.setLastLoggedIn(new Date(Instant.now()
                .getEpochSecond()));
            logger.info("Updating user with last logged in time");
            userService.updateUser(user);
            token.setRefreshToken(existingRefreshToken.getToken());
        } else {
            throw new LiveChatException("User not found", HttpStatus.UNAUTHORIZED);
        }
        return token;
    }
}
