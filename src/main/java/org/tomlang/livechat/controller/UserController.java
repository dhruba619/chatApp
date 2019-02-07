package org.tomlang.livechat.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.tomlang.livechat.entities.User;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.AvatarUploadResponse;
import org.tomlang.livechat.json.NotificationSettings;
import org.tomlang.livechat.json.UpdatePasswordRequest;
import org.tomlang.livechat.json.UpdatePasswordResponse;
import org.tomlang.livechat.json.UserRequest;
import org.tomlang.livechat.json.UserUpdateProfileRequest;
import org.tomlang.livechat.service.EmailService;
import org.tomlang.livechat.service.FileStorageService;
import org.tomlang.livechat.service.UserService;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;
    
    @Autowired
    FileStorageService fileStorageService;
    
    

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(path = "/api/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> signUp(@RequestBody UserRequest request, HttpServletRequest httpRequest) throws Exception {
        logger.info("Initiating user signup.");
        try {
            validateUserRequest(request);
            User user = userService.createUser(request);
            String url = httpRequest.getRequestURL().toString();
            String emailText = new StringBuffer(url).append("/confirm/")
                .append(user.getConfirmEmailHash())
                .toString();
            logger.info("Email body for confirmation: "+emailText);
            emailService.send(user.getEmail(), "Live Chat Email Confirmation", emailText);

            return ResponseEntity.ok()
                .body(user);
        } catch (Exception e) {
            if (e instanceof LiveChatException) {
                throw e;
            } else {
                throw new LiveChatException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private void validateUserRequest(UserRequest request) throws LiveChatException {
        boolean invalid = false;
        if(null==request.getEmail() || request.getEmail().equals("")) {
            invalid=true;
        }
        if(null==request.getFull_name() || request.getFull_name().equals("")) {
            invalid=true;
        }
        if(null==request.getPassword() || request.getPassword().equals("")) {
            invalid=true;
        }
        if(invalid) {
            throw new LiveChatException("Missing required parameter", HttpStatus.BAD_REQUEST);
        }
        
    }

    @RequestMapping(path = "/api/signup/confirm/{hash}", method = RequestMethod.GET)
    public ResponseEntity<String> confirmEmail(@PathVariable("hash") String confirmHash) throws LiveChatException {
        try {
            User user = userService.findByHash(confirmHash);
            if (null != user) {
                user.setHasConfirmedEmail(true);
                userService.updateUser(user);
                return ResponseEntity.ok()
                    .body("Your Email has been confirmed successfuly");
            }
            return ResponseEntity.ok()
                .body("COULD NOT FIND VALID USER");

        } catch (Exception e) {
            if (e instanceof LiveChatException) {
                throw e;
            } else {
                throw new LiveChatException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    }
    
    @PutMapping("/api/secured/user/upload_avatar")
    public AvatarUploadResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String authToken) throws LiveChatException {
        String fileName = fileStorageService.storeFile(file);
        userService.updateAvatar(fileName, authToken);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName)
                .toUriString();

        return new AvatarUploadResponse("success", fileDownloadUri);
    }
    
    @PutMapping("/api/secured/user/updateProfile")
    public UserUpdateProfileRequest updateUserProfile(@RequestBody UserUpdateProfileRequest request, @RequestHeader("Authorization") String authToken) throws LiveChatException {
       validateUserProfileRequest(request);
        userService.updateUserProfile(request, authToken);
        return request;

    }
    
    @PutMapping("/api/secured/user/updateNotificationSettings")
    public NotificationSettings updateUserNotifications(@RequestBody NotificationSettings request, @RequestHeader("Authorization") String authToken) throws LiveChatException {
       
        userService.updateUserNotificationSettings(request, authToken);
        return request;

    }
    
    @PutMapping("/api/secured/user/updatePassword")
    public UpdatePasswordResponse updateUserPassword(@RequestBody UpdatePasswordRequest request, @RequestHeader("Authorization") String authToken) throws LiveChatException {
       validatePasswordRequest(request);
        return userService.updateUserPassword(request, authToken);
    }
   

    private void validatePasswordRequest(UpdatePasswordRequest request) throws LiveChatException {
        boolean invalid = false;
        if(null==request.getNewPassword()|| request.getNewPassword().equals("")) {
            invalid=true;
        }
        if(null==request.getOldPassword() || request.getNewPassword().equals("")) {
            invalid=true;
        }
        if(invalid) {
            throw new LiveChatException("Missing required parameter", HttpStatus.BAD_REQUEST);
        }
        
    }

    private void validateUserProfileRequest(UserUpdateProfileRequest request) throws LiveChatException {
        boolean invalid = false;
        if(null==request.getFullName() || request.getFullName().equals("")) {
            invalid=true;
        }
        if(null==request.getLanguage() || request.getLanguage().equals("")) {
            invalid=true;
        }
        if(invalid) {
            throw new LiveChatException("Missing required parameter", HttpStatus.BAD_REQUEST);
        }
        
    }
}
