package org.tomlang.livechat.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tomlang.livechat.entities.User;
import org.tomlang.livechat.entities.UserAppDetails;
import org.tomlang.livechat.enums.SupportedLanguages;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.NotificationSettings;
import org.tomlang.livechat.json.UpdatePasswordRequest;
import org.tomlang.livechat.json.UpdatePasswordResponse;
import org.tomlang.livechat.json.UserAppDetailsResponse;
import org.tomlang.livechat.json.UserRequest;
import org.tomlang.livechat.json.UserUpdateProfileRequest;
import org.tomlang.livechat.repositories.UserAppDetailsRepository;
import org.tomlang.livechat.repositories.UserRepository;
import org.tomlang.livechat.util.PasswordEncoder;
import org.tomlang.livechat.util.TokenProvider;

import com.google.gson.Gson;

@Service
public class UserService {
    
    @Autowired
    private AppDetailsService appDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    private UserAppDetailsRepository userAppDetailsRepository;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    public User createUser(UserRequest request) throws NoSuchAlgorithmException, LiveChatException {
        try {

            logger.info("Initiating Create new user...");

            logger.info("Encoding password...");
            String passwordSalt = PasswordEncoder.createRandomPasswordSalt();
            String encodedPassword = PasswordEncoder.encodePassword(request.getPassword(), passwordSalt);

            User user = new User();
            user.setConfirmEmailHash(createEmailConfirmationHash(request.getEmail(), request.getFullName()));
            user.setEmail(request.getEmail());
            user.setFullName(request.getFullName());
            user.setPassword(encodedPassword);
            user.setPasswordSalt(passwordSalt);
            user.setJoinedDate(new Date(Instant.now()
                .toEpochMilli()));
            if(null!=request.getLanguage()) {
            user.setLanguage(request.getLanguage());
            } else {
                user.setLanguage(SupportedLanguages.eng);
            }
            logger.info("Creating joined date");
            logger.info("User: " + request.getEmail() + "Joined on: " + user.getJoinedDate());

            user.setHasConfirmedEmail(true);
            userRepository.save(user);
            return user;
        } catch (ConstraintViolationException ex) {
            throw new LiveChatException("User Already exists", HttpStatus.BAD_REQUEST);
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmailAddress(email)
            .get();
    }

    public Optional<User> getUserbyId(Integer id) {

        return userRepository.findById(id);
    }

    public User findByHash(String hash) {
        return userRepository.findByEmailHash(hash);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public UserAppDetails getByUserAndAppDetails(Integer userId, Integer appdetailsId) {
        return userAppDetailsRepository.getByUserIdAndAppDetail(userId, appdetailsId);
    }

    public void pingUser(Integer userId, Integer appdetailsId) {

    }

    private String createEmailConfirmationHash(String email, String username) throws NoSuchAlgorithmException {

        String input = email + username;
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);

        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        hashtext = hashtext.substring(0, 30);
        return hashtext;
    }

    public void updateAvatar(String fileName, String authToken) {
        // Token token = tokenRepository.findByToken(authToken);
        User user = getUserbyId(tokenProvider.getUserIdFromJwt(authToken)).get();

        user.setImage("/download/" + fileName);

        userRepository.save(user);

    }

    public void updateUserProfile(UserUpdateProfileRequest request, String authToken) {
        User user = getUserbyId(tokenProvider.getUserIdFromJwt(authToken)).get();
        if(null!=request.getBiography() || request.getBiography()!="") {
            user.setBiography(request.getBiography());
        }
        if(null!=request.getFullName() || request.getFullName()!="") {
            user.setFullName(request.getFullName());
        }
        if(null!=request.getLanguage() || request.getBiography()!="") {
            user.setLanguage(request.getLanguage());
        }
        if(null!=request.getSocialLinks()) {
            String socialLinks = new Gson().toJson(request.getSocialLinks());
            user.setSocialLinks(socialLinks);
        }
        userRepository.save(user);
        
    }

    public void updateUserNotificationSettings(NotificationSettings request, String authToken) {
        User user = getUserbyId(tokenProvider.getUserIdFromJwt(authToken)).get();
        String settings = new Gson().toJson(request);
        user.setNotificationSettings(settings);
        userRepository.save(user);
        
    }

    public UpdatePasswordResponse updateUserPassword(UpdatePasswordRequest request, String authToken) {
        logger.info("Encoding password...");
        User user = getUserbyId(tokenProvider.getUserIdFromJwt(authToken)).get();
        String passwordSalt = user.getPasswordSalt();
        String encodedOldPassword = PasswordEncoder.encodePassword(request.getOldPassword(), passwordSalt);
        if(user.getPassword().equals(encodedOldPassword)) {
            user.setPassword(PasswordEncoder.encodePassword(request.getNewPassword(), passwordSalt));
            userRepository.save(user);
            UpdatePasswordResponse response = new UpdatePasswordResponse();
                response.setWasSuccessful(true);
            return response;
        }
        else {
            UpdatePasswordResponse response = new UpdatePasswordResponse();
                response.setWasSuccessful(false);
            return response;
        }
       
    }
    
    public List<UserAppDetailsResponse> getUserAppDetails(String authToken) {
        Integer userId = tokenProvider.getUserIdFromJwt(authToken);
        return appDetailsService.getAppDetailsByUser(userId);
    }

}
