package org.tomlang.livechat.service;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tomlang.livechat.entities.App;
import org.tomlang.livechat.entities.Token;
import org.tomlang.livechat.entities.User;
import org.tomlang.livechat.entities.UserAppDetails;
import org.tomlang.livechat.entities.UserAppInvitedDetails;
import org.tomlang.livechat.enums.Role;
import org.tomlang.livechat.enums.UserStatus;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.AcceptInviteRequest;
import org.tomlang.livechat.json.AcceptInviteResponse;
import org.tomlang.livechat.json.AppTeamResponse;
import org.tomlang.livechat.json.CreateTeamRequest;
import org.tomlang.livechat.json.SignUpInviteRequest;
import org.tomlang.livechat.json.UserRequest;
import org.tomlang.livechat.repositories.AppDetailsRepository;
import org.tomlang.livechat.repositories.AppRepository;
import org.tomlang.livechat.repositories.UserAppDetailsInvitedRepository;
import org.tomlang.livechat.repositories.UserAppDetailsRepository;
import org.tomlang.livechat.util.TimeZoneUtil;
import org.tomlang.livechat.util.TokenProvider;

@Service
public class AppTeamService {

    @Autowired
    UserAppDetailsRepository userAppDetailsRepository;

    @Autowired
    AppDetailsRepository appDetailsRepository;

    @Autowired
    AppRepository appRepository;

    @Autowired
    AppService appService;

    @Autowired
    UserService userService;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    TimeZoneUtil timeZoneUtil;

    @Autowired
    UserAppDetailsInvitedRepository userAppDetailsInvitedRepository;

    @Value("${app.invitation.code.ttl}")
    private String inviteTTL;

    @Autowired
    EmailService emailService;

    @Autowired
    LoginService loginService;

    public void proccessCreateTeam(String authToken, String appHashCode, CreateTeamRequest request, AppTeamResponse response) throws LiveChatException {
        // validate role

        App app = appService.getAppByHashToken(appHashCode);
        if (request.getRole() == null)
            throw new LiveChatException("Missing required param role", HttpStatus.BAD_REQUEST);

        Role roleEnum = null;
        if (request.getRole() == 0)
            roleEnum = Role.AGENT;
        if (request.getRole() == 1)
            roleEnum = Role.ADMIN;
        if (request.getRole() == 2)
            roleEnum = Role.OWNER;

        if (roleEnum.equals(Role.OWNER))
            throw new LiveChatException("Role not allowed", HttpStatus.BAD_REQUEST);

        // check with email if user exists and has user app details throw error

        User user = null;
        try {
            user = userService.getUserByEmail(request.getEmail());
        } catch (Exception e) {

        }
        if (null != user) {
            List<UserAppDetails> userAppDetails = userAppDetailsRepository.getByUserIdAndAppDetail(user.getId());
            if (userAppDetails.size() > 0) {
                for (UserAppDetails detail : userAppDetails) {

                    if (app.getAppDetailsId()
                        .equals(detail.getAppDetailsId()))
                        throw new LiveChatException("User already member of the appapp", HttpStatus.BAD_REQUEST);
                }
            }
        }

        // check in invited details if a valid invitation exists throw error
        List<UserAppInvitedDetails> invitedDetails = userAppDetailsInvitedRepository.getInvitedDetailsByEmail(request.getEmail());
        if (invitedDetails.size() > 0) {
            // Check for valid invitation
            Long currentTime = Instant.now()
                .toEpochMilli();
            for (UserAppInvitedDetails inviDetail : invitedDetails) {
                if (currentTime - inviDetail.getTtl() < Long.valueOf(inviteTTL))
                    throw new LiveChatException("User Already has a valid invitation waiting to be accepted", HttpStatus.BAD_REQUEST);
            }

        }
        // check if user exists and no user app details exisits or user doesnot exisit
        // then create a row in user_app_invited_details and also add a row in user_app_details with status as
        // INVITED and with the id of the user_app_invited_details
        // send email to user with invitation code and validity
        // return app team response

        String inviteCode = UUID.randomUUID()
            .toString()
            .replaceAll("-", "")
            .toUpperCase();

        UserAppInvitedDetails newUserAppInvitedDetail = new UserAppInvitedDetails();
        newUserAppInvitedDetail.setEmail(request.getEmail());
        newUserAppInvitedDetail.setInvitationCode(inviteCode);
        newUserAppInvitedDetail.setName(request.getName());
        newUserAppInvitedDetail.setTtl(Instant.now()
            .toEpochMilli());
        newUserAppInvitedDetail = userAppDetailsInvitedRepository.save(newUserAppInvitedDetail);

        UserAppDetails newUserAppDetails = new UserAppDetails();
        newUserAppDetails.setAppDetailsId(app.getAppDetailsId());
        newUserAppDetails.setAway(true);
        newUserAppDetails.setChatTitle(request.getChatTitle());
        newUserAppDetails.setInvitedDetailsId(newUserAppInvitedDetail.getId());
        newUserAppDetails.setRole(roleEnum);

        if (null != user)
            newUserAppDetails.setUserId(user.getId());

        newUserAppDetails.setUserStatus(UserStatus.INVITED);
        userAppDetailsRepository.save(newUserAppDetails);
        String emailText = "Please use this invitation code: " + inviteCode;
        emailService.send(request.getEmail(), "You have been invited to Live Chat Application", emailText);

        response.setChatTitle(request.getChatTitle());
        response.setEmail(request.getEmail());
        response.setId(newUserAppInvitedDetail.getId());
        if (null != user)
            response.setImage(user.getImage());
        response.setName(request.getName());
        response.setRole(request.getRole()
            .toString());
        response.setStatus(newUserAppDetails.getUserStatus()
            .toString());

    }

    public Token signupInvite(SignUpInviteRequest request) throws LiveChatException, NoSuchAlgorithmException {

        // Search user app invited details with the invitation code from
        UserAppInvitedDetails userAppInvitedDetails = userAppDetailsInvitedRepository.getInvitedDetailsByCode(request.getInvitationCode());
        if (null == userAppInvitedDetails)
            throw new LiveChatException("Invalid Code", HttpStatus.BAD_REQUEST);
        // check if the invitation code is valid
        if (Instant.now()
            .toEpochMilli() - userAppInvitedDetails.getTtl() > Long.valueOf(inviteTTL))
            throw new LiveChatException("Invalid Code Expired", HttpStatus.BAD_REQUEST);
        // create user
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(userAppInvitedDetails.getEmail());
        userRequest.setFullName(request.getFullName());
        userRequest.setPassword(request.getPassword());
        User user = userService.createUser(userRequest);
        String emailText = "Welcome";
        emailService.send(user.getEmail(), "Welcome to Live Chat Application", emailText);
        // delete invitation row
        userAppDetailsInvitedRepository.deleteByCode(request.getInvitationCode());

        // delete the invited id from user app details
        // update user id user app details
        // set status of user in user app details to OFFLINE
        UserAppDetails userAppDetails = userAppDetailsRepository.getByInvitedId(userAppInvitedDetails.getId());
        userAppDetails.setUserId(user.getId());
        userAppDetails.setInvitedDetailsId(null);
        userAppDetails.setUserStatus(UserStatus.OFFLINE);
        userAppDetailsRepository.save(userAppDetails);

        // login the user and send response
        return loginService.generateToken(user.getEmail(), request.getPassword());

    }

    public AcceptInviteResponse acceptInvite(String authToken, AcceptInviteRequest request) throws LiveChatException {
        // Search user app invited details with the invitation code from
        UserAppInvitedDetails userAppInvitedDetails = userAppDetailsInvitedRepository.getInvitedDetailsByCode(request.getInvitationCode());
        if (null == userAppInvitedDetails)
            throw new LiveChatException("Invalid Code", HttpStatus.BAD_REQUEST);
        // check if the invitation code is valid
        if (Instant.now()
            .toEpochMilli() - userAppInvitedDetails.getTtl() > Long.valueOf(inviteTTL))
            throw new LiveChatException("Invalid Code Expired", HttpStatus.BAD_REQUEST);
        User user = userService.getUserbyId(tokenProvider.getUserIdFromJwt(authToken))
            .get();
        userAppDetailsInvitedRepository.deleteByCode(request.getInvitationCode());
        // delete the invited id from user app details
        // update user id user app details
        // set status of user in user app details to OFFLINE
        UserAppDetails userAppDetails = userAppDetailsRepository.getByInvitedId(userAppInvitedDetails.getId());
        userAppDetails.setUserId(user.getId());
        userAppDetails.setInvitedDetailsId(null);
        userAppDetails.setUserStatus(UserStatus.OFFLINE);
        userAppDetailsRepository.save(userAppDetails);
        App app = appRepository.findByAppDetailId(userAppDetails.getAppDetailsId());
        AcceptInviteResponse response = new AcceptInviteResponse();
        response.setAppHash(app.getAppHashcode());
        return response;
    }

}
