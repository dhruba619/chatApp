package org.tomlang.livechat.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.tomlang.livechat.entities.Token;
import org.tomlang.livechat.entities.User;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.AcceptInviteRequest;
import org.tomlang.livechat.json.AcceptInviteResponse;
import org.tomlang.livechat.json.AppRequest;
import org.tomlang.livechat.json.AvatarUploadResponse;
import org.tomlang.livechat.json.LastAppResponse;
import org.tomlang.livechat.json.NotificationSettings;
import org.tomlang.livechat.json.SignUpInviteRequest;
import org.tomlang.livechat.json.UpdatePasswordRequest;
import org.tomlang.livechat.json.UpdatePasswordResponse;
import org.tomlang.livechat.json.UserAppDetailsResponse;
import org.tomlang.livechat.json.UserHasAppRequest;
import org.tomlang.livechat.json.UserHasAppResponse;
import org.tomlang.livechat.json.UserRequest;
import org.tomlang.livechat.json.UserUpdateProfileRequest;
import org.tomlang.livechat.service.AppService;
import org.tomlang.livechat.service.AppTeamService;
import org.tomlang.livechat.service.EmailService;
import org.tomlang.livechat.service.FileStorageService;
import org.tomlang.livechat.service.LoginService;
import org.tomlang.livechat.service.UserAppService;
import org.tomlang.livechat.service.UserService;
import org.tomlang.livechat.util.TimeZoneUtil;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    AppService appService;

    @Autowired
    UserAppService userAppService;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    LoginService loginService;

    @Autowired
    TimeZoneUtil timeZoneUtil;
    
    @Autowired
    AppTeamService appTeamService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(path = "/api/user/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Token> signUp(@RequestBody UserRequest request, HttpServletRequest httpRequest) throws Exception {
        logger.info("Initiating user signup.");
        try {
            validateUserRequest(request);
            User user = userService.createUser(request);
            String url = httpRequest.getRequestURL()
                .toString();
            String emailText = "Welcome";
            logger.info("Email body for confirmation: " + emailText);
            emailService.send(user.getEmail(), "Welcome to Live Chat Application", emailText);
            /**UserResponse response = new UserResponse();
            response.setBiography(user.getBiography());
            response.setEmail(user.getEmail());
            response.setFullName(user.getFullName());
            response.setId(user.getId());
            response.setImage(user.getImage());
            response.setJoinedDate(user.getJoinedDate());
            response.setLanguage(user.getLanguage());
            response.setLastLoggedIn(user.getLastLoggedIn());
            response.setNotificationSettings(new Gson().fromJson(user.getNotificationSettings(), NotificationSettings.class));
            response.setSocialLinks(new Gson().fromJson(user.getSocialLinks(), SocialLinks.class));**/

            // Login the user
            Token token = loginService.generateToken(request.getEmail(), request.getPassword());

            // create the app for the user
            AppRequest appRequest = new AppRequest();
            appRequest.setName(request.getAppName());
            appRequest.setTimeZone(timeZoneUtil.getDefault());
            appService.createApp(appRequest, token.getToken());
            return ResponseEntity.ok()
                .body(token);
        } catch (Exception e) {
            if (e instanceof LiveChatException) {
                throw e;
            } else {
                throw new LiveChatException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
    
    @RequestMapping(path = "/api/user/signup_invite", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Token> signUpinvitee(@RequestBody SignUpInviteRequest request, HttpServletRequest httpRequest) throws Exception {
        return ResponseEntity.ok()
            .body(appTeamService.signupInvite(request));
    }
    
    
    @PostMapping("/api/secured/user/private/accept_invitation")
    public ResponseEntity<AcceptInviteResponse> acceptInvite(@RequestHeader("Authorization") String authToken, @RequestBody AcceptInviteRequest request, HttpServletRequest httpRequest) throws Exception {
        return ResponseEntity.ok()
            .body(appTeamService.acceptInvite(authToken, request));
    }

    private void validateUserRequest(UserRequest request) throws LiveChatException {
        boolean invalid = false;
        if (null == request.getEmail() || request.getEmail()
            .equals("")) {
            invalid = true;
        }
        if (null == request.getFullName() || request.getFullName()
            .equals("")) {
            invalid = true;
        }
        if (null == request.getPassword() || request.getPassword()
            .equals("")) {
            invalid = true;
        }

        if (null == request.getAppName() || request.getAppName()
            .equals("")) {
            invalid = true;
        }
        if (invalid) {
            throw new LiveChatException("Missing required parameter", HttpStatus.BAD_REQUEST);
        }

    }

    @Deprecated
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

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/download/")
            .path(fileName)
            .toUriString();
        userService.updateAvatar(fileDownloadUri, authToken);
        return new AvatarUploadResponse("success", fileDownloadUri);
    }

    @PutMapping("/api/secured/user/private/profile")
    public UserUpdateProfileRequest updateUserProfile(@RequestBody UserUpdateProfileRequest request, @RequestHeader("Authorization") String authToken) throws LiveChatException {
        validateUserProfileRequest(request);
        userService.updateUserProfile(request, authToken);
        return request;

    }

    @PutMapping("/api/secured/user/private/notification_settings")
    public NotificationSettings updateUserNotifications(@RequestBody NotificationSettings request, @RequestHeader("Authorization") String authToken) throws LiveChatException {

        userService.updateUserNotificationSettings(request, authToken);
        return request;

    }

    @PutMapping("/api/secured/user/private/password")
    public UpdatePasswordResponse updateUserPassword(@RequestBody UpdatePasswordRequest request, @RequestHeader("Authorization") String authToken) throws LiveChatException {
        validatePasswordRequest(request);
        return userService.updateUserPassword(request, authToken);
    }

    @GetMapping("/api/secured/user/apps")
    public List<UserAppDetailsResponse> getAppDetailsForUser(@RequestHeader("Authorization") String authToken) {
        return userService.getUserAppDetails(authToken);
    }

    @GetMapping("/api/secured/user/private/last_app")
    public LastAppResponse getLastApp(@RequestHeader("Authorization") String authToken) throws LiveChatException {

        LastAppResponse response = new LastAppResponse();
        response.setAppHash(userAppService.getLastPingedAppsByUser(authToken));

        return response;
    }
    
    @GetMapping("/api/secured/user/private/has_app")
    public UserHasAppResponse hasApp(@RequestHeader("Authorization") String authToken, @RequestBody UserHasAppRequest request) throws LiveChatException {

        UserHasAppResponse response = new UserHasAppResponse();
        response.setHasApp(userAppService.hasUserApp(authToken,request.getAppHash()));

        return response;
    }

    private void validatePasswordRequest(UpdatePasswordRequest request) throws LiveChatException {
        boolean invalid = false;
        if (null == request.getNewPassword() || request.getNewPassword()
            .equals("")) {
            invalid = true;
        }
        if (null == request.getOldPassword() || request.getNewPassword()
            .equals("")) {
            invalid = true;
        }
        if (invalid) {
            throw new LiveChatException("Missing required parameter", HttpStatus.BAD_REQUEST);
        }

    }

    private void validateUserProfileRequest(UserUpdateProfileRequest request) throws LiveChatException {
        boolean invalid = false;
        if (null == request.getFullName() || request.getFullName()
            .equals("")) {
            invalid = true;
        }
        if (null == request.getLanguage() || request.getLanguage()
            .equals("")) {
            invalid = true;
        }
        if (invalid) {
            throw new LiveChatException("Missing required parameter", HttpStatus.BAD_REQUEST);
        }

    }
}
