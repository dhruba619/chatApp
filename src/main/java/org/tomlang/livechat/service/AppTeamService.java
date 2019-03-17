package org.tomlang.livechat.service;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tomlang.livechat.entities.App;
import org.tomlang.livechat.entities.AppDetails;
import org.tomlang.livechat.entities.Token;
import org.tomlang.livechat.entities.User;
import org.tomlang.livechat.entities.UserAppDetails;
import org.tomlang.livechat.entities.UserAppInvitedDetails;
import org.tomlang.livechat.enums.Role;
import org.tomlang.livechat.enums.UserStatus;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.AcceptInviteRequest;
import org.tomlang.livechat.json.AcceptInviteResponse;
import org.tomlang.livechat.json.ActivateDeactivateUninviteTeamRequest;
import org.tomlang.livechat.json.AppTeamResponse;
import org.tomlang.livechat.json.AppTeamResponseSimplified;
import org.tomlang.livechat.json.CreateTeamRequest;
import org.tomlang.livechat.json.SignUpInviteRequest;
import org.tomlang.livechat.json.UpdateTeamMember;
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

    public void activateDeactivateTeamMember(String authToken, String appHashCode, ActivateDeactivateUninviteTeamRequest request, boolean isReactivate) throws LiveChatException {
        App app = appService.getAppByHashToken(appHashCode);
        AppDetails appDetails = appDetailsRepository.findById(app.getAppDetailsId())
            .get();

        User actionUser = userService.getUserbyId(tokenProvider.getUserIdFromJwt(authToken))
            .get();
        UserAppDetails actionUserAppDetails = userAppDetailsRepository.getByUserIdAndAppDetail(actionUser.getId(), appDetails.getId());
        // check if targetUser is present
        Optional<User> targetUserOptional = userService.getUserbyId(request.getId());
        if (targetUserOptional.isPresent()) {
            User targetUser = targetUserOptional.get();
            UserAppDetails targetUserAppDetails = userAppDetailsRepository.getByUserIdAndAppDetail(targetUser.getId(), appDetails.getId());

            if (!isReactivate) {
                // checks for deactivation
                if (Role.OWNER.equals(targetUserAppDetails.getRole())) {
                    new LiveChatException("Owner cannot be deactivated", HttpStatus.BAD_REQUEST);
                }
                if (Role.OWNER.equals(actionUserAppDetails.getRole())) {
                    if (Role.ADMIN.equals(targetUserAppDetails.getRole()) || Role.AGENT.equals(targetUserAppDetails.getRole())) {
                        targetUserAppDetails.setUserStatus(UserStatus.DEACTIVATED);
                        userAppDetailsRepository.save(targetUserAppDetails);
                        return;
                    }
                }

                if (Role.ADMIN.equals(actionUserAppDetails.getRole())) {
                    if (Role.ADMIN.equals(targetUserAppDetails.getRole()) || Role.AGENT.equals(targetUserAppDetails.getRole())) {
                        targetUserAppDetails.setUserStatus(UserStatus.DEACTIVATED);
                        userAppDetailsRepository.save(targetUserAppDetails);
                        return;
                    }
                }
            } else {
                targetUserAppDetails.setUserStatus(UserStatus.OFFLINE);
                userAppDetailsRepository.save(targetUserAppDetails);
            }
        }
    }

    public void uninviteTeamMember(String authToken, String appHashCode, ActivateDeactivateUninviteTeamRequest request) throws LiveChatException {
        App app = appService.getAppByHashToken(appHashCode);
        AppDetails appDetails = appDetailsRepository.findById(app.getAppDetailsId())
            .get();
        UserAppDetails actionUserAppDetails = userAppDetailsRepository.getByInvitedId(request.getId());
        if (UserStatus.INVITED.equals(actionUserAppDetails.getUserStatus())) {
            UserAppInvitedDetails invitedDetails = userAppDetailsInvitedRepository.findById(actionUserAppDetails.getInvitedDetailsId())
                .get();
            invitedDetails.setInvitationCode("INVALID");
            actionUserAppDetails.setUserStatus(UserStatus.DEACTIVATED);
            userAppDetailsRepository.save(actionUserAppDetails);
            userAppDetailsInvitedRepository.save(invitedDetails);
            userAppDetailsRepository.deleteById(actionUserAppDetails.getId());
            userAppDetailsInvitedRepository.deleteById(invitedDetails.getId());
        } else {
            throw new LiveChatException("User not in invited status, cannot be univited", HttpStatus.BAD_REQUEST);
        }
    }

    public UpdateTeamMember updateTeamMember(String authToken, String appHashCode, UpdateTeamMember request) throws LiveChatException {
        App app = appService.getAppByHashToken(appHashCode);
        AppDetails appDetails = appDetailsRepository.findById(app.getAppDetailsId())
            .get();

        User actionUser = userService.getUserbyId(tokenProvider.getUserIdFromJwt(authToken))
            .get();
        UserAppDetails actionUserAppDetails = userAppDetailsRepository.getByUserIdAndAppDetail(actionUser.getId(), appDetails.getId());
        UserAppDetails targetUserAppDetails = userAppDetailsRepository.getByUserIdAndAppDetail(request.getId(), appDetails.getId());
        targetUserAppDetails.setChatTitle(request.getChatTitle());
        if (Role.OWNER.equals(actionUserAppDetails.getRole())) {

            targetUserAppDetails.setRole(request.getRole());
            if (Role.OWNER.equals(request.getRole())) {
                actionUserAppDetails.setRole(Role.ADMIN);
                userAppDetailsRepository.save(actionUserAppDetails);
            }
            userAppDetailsRepository.save(targetUserAppDetails);

        } else if (Role.ADMIN.equals(actionUserAppDetails.getRole())) {
            if (Role.OWNER.equals(request.getRole())) {
                throw new LiveChatException("Admin cannot make someone owner", HttpStatus.BAD_REQUEST);
            }
            targetUserAppDetails.setRole(request.getRole());
            userAppDetailsRepository.save(targetUserAppDetails);
        }

        return request;
    }

    public List<AppTeamResponseSimplified> getAppTeamMembersSimlified(String authToken, String appHashCode) {
        // find app by hashcode
        // get all UserAppDetails where appdetails id from the found app
        List<AppTeamResponseSimplified> reponses = new ArrayList<>();

        App app = appRepository.findByAppHashToken(appHashCode);
        List<UserAppDetails> userAppDetailsList = userAppDetailsRepository.getByAppDetailId(app.getAppDetailsId());

        for (UserAppDetails detail : userAppDetailsList) {
            User user = null;
            if (null != detail.getUserId())
                user = userService.getUserbyId(detail.getUserId())
                    .get();
            AppTeamResponseSimplified response = new AppTeamResponseSimplified();
            response.setId(detail.getUserId());
            if (null != detail.getUserStatus())
                response.setStatus(detail.getUserStatus()
                    .toString());
            if (null != detail.getUserStatus() && detail.getUserStatus()
                .equals(UserStatus.INVITED)) {
                if (null != user) {
                    response.setImage(user.getImage());
                    response.setName(user.getFullName());
                } else {
                    UserAppInvitedDetails invd = userAppDetailsInvitedRepository.findById(detail.getInvitedDetailsId())
                        .get();
                    if (null != invd) {
                        response.setName(invd.getName());
                    }
                }
            } else {
                response.setImage(user.getImage());
                response.setName(user.getFullName());
            }
            reponses.add(response);
        }
        return reponses;
    }
}
