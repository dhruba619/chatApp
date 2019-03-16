package org.tomlang.livechat.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.tomlang.livechat.entities.App;
import org.tomlang.livechat.entities.AppDetails;
import org.tomlang.livechat.entities.User;
import org.tomlang.livechat.entities.UserAppDetails;
import org.tomlang.livechat.enums.UserStatus;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.AppSpecificInfo;
import org.tomlang.livechat.json.IsAwayPayload;
import org.tomlang.livechat.json.NotificationSettings;
import org.tomlang.livechat.json.SocialLinks;
import org.tomlang.livechat.json.UserPrivateInformation;
import org.tomlang.livechat.repositories.AppDetailsRepository;
import org.tomlang.livechat.repositories.AppRepository;
import org.tomlang.livechat.repositories.UserAppDetailsRepository;
import org.tomlang.livechat.util.TokenProvider;

import com.google.gson.Gson;

@Service
public class UserAppService {

    Logger logger = LoggerFactory.getLogger(UserAppService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AppService appService;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    private UserAppDetailsRepository userAppDetailsRepository;

    @Autowired
    private AppDetailsRepository appDetailsRepository;

    @Autowired
    private AppRepository appRepository;

    @Value("${app.livechat.lastpinnged.seconds}")
    private long lastPingedTTL;

    @Async("threadPoolTaskExecutor")
    public void processUserPing(String authToken, String appHash) {

        try {
            App app = appService.getAppByHashToken(appHash);

            // Token token = tokenRepository.findByToken(authToken);
            User user = userService.getUserbyId(tokenProvider.getUserIdFromJwt(authToken))
                .get();

            UserAppDetails details = userService.getByUserAndAppDetails(user.getId(), app.getAppDetailsId());

            details.setLastPinged(Instant.now()
                .toEpochMilli());
            userAppDetailsRepository.save(details);

        } catch (LiveChatException e) {
            logger.error("Failed to ping: " + e.getErrorDesciption());
        }
    }

    public void updateAwayStatus(IsAwayPayload payload, String authToken, String appHashCode) {
        try {
            App app = appService.getAppByHashToken(appHashCode);

            // Token token = tokenRepository.findByToken(authToken);
            User user = userService.getUserbyId(tokenProvider.getUserIdFromJwt(authToken))
                .get();

            UserAppDetails details = userService.getByUserAndAppDetails(user.getId(), app.getAppDetailsId());
            details.setAway(payload.isAwayValue());
            userAppDetailsRepository.save(details);

        } catch (LiveChatException e) {
            logger.error("Failed to update away status: " + e.getErrorDesciption());
        }

    }

    public UserPrivateInformation getUserPrivateInformationForApp(String authToken, String appHashCode) throws LiveChatException {
        App app = appService.getAppByHashToken(appHashCode);

        // Token token = tokenRepository.findByToken(authToken);
        User user = userService.getUserbyId(tokenProvider.getUserIdFromJwt(authToken))
            .get();

        UserAppDetails details = userService.getByUserAndAppDetails(user.getId(), app.getAppDetailsId());

        UserPrivateInformation information = new UserPrivateInformation();
        information.setId(user.getId());
        information.setFullName(user.getFullName());
        information.setImage(user.getImage());
        information.setLastLoggedIn(user.getLastLoggedIn()
            .toString());

        Gson gson = new Gson();

        information.setNotificationSettings(gson.fromJson(user.getNotificationSettings(), NotificationSettings.class));
        information.setSocialLinks(gson.fromJson(user.getSocialLinks(), SocialLinks.class));

        // Calculate role and status

        information.setRole(details.getRole());

        information.setStatus(deriveStatus(details));
        return information;

    }

    private UserStatus deriveStatus(UserAppDetails details) {

        if (null != details.getUserStatus()) {
            if (details.getUserStatus()
                .equals(UserStatus.DEACTIVATED)) {
                return UserStatus.DEACTIVATED;
            }

        }

        if (details.isAway()) {
            return UserStatus.AWAY;
        }
        if (null != details.getLastPinged()) {
            if (Instant.now()
                .toEpochMilli() - details.getLastPinged() < lastPingedTTL * 1000) {
                if (details.isAway()) {

                    return UserStatus.AWAY;
                }
                return UserStatus.ONLINE;
            }

            if (Instant.now()
                .toEpochMilli() - details.getLastPinged() > lastPingedTTL * 1000) {
                return UserStatus.OFFLINE;
            }
        }

        return UserStatus.OFFLINE;
    }

    public AppSpecificInfo getInformationForApp(String authToken, String appHashCode) throws LiveChatException {
        App app = appService.getAppByHashToken(appHashCode);
        Optional<AppDetails> appDetailsOptional = appDetailsRepository.findById(app.getAppDetailsId());

        AppDetails appDetails = appDetailsOptional.get();

        // Token token = tokenRepository.findByToken(authToken);
        User user = userService.getUserbyId(tokenProvider.getUserIdFromJwt(authToken))
            .get();

        UserAppDetails details = userService.getByUserAndAppDetails(user.getId(), app.getAppDetailsId());

        AppSpecificInfo info = new AppSpecificInfo();

        info.setDescription(appDetails.getDescription());
        info.setName(appDetails.getName());
        info.setImage(appDetails.getImage());
        info.setSocialLinks(appDetails.getSocialLinks());
        info.setTimeZone(appDetails.getTimeZone());
        return info;
    }

    /**
     * Return last pinged app for the user
     */
    public String getLastPingedAppsByUser(String authToken) {
        User user = userService.getUserbyId(tokenProvider.getUserIdFromJwt(authToken))
            .get();
        List<UserAppDetails> details = userAppDetailsRepository.getUserLastPingedUserAppDetails(user.getId());
        UserAppDetails detail = details.get(0);
        if (null != detail.getLastPinged()) {
            App app = appRepository.findByAppDetailId(detail.getAppDetailsId());
            return app.getAppHashcode();
        } else {
            details = userAppDetailsRepository.getUserFirstUserAppDetails(user.getId());
            detail = details.get(0);
            App app = appRepository.findByAppDetailId(detail.getAppDetailsId());
            return app.getAppHashcode();

        }

    }

    public boolean hasUserApp(String authToken, String appHash) throws LiveChatException {
        App app = appService.getAppByHashToken(appHash);
        Optional<AppDetails> appDetailsOptional = appDetailsRepository.findById(app.getAppDetailsId());
        User user = userService.getUserbyId(tokenProvider.getUserIdFromJwt(authToken))
            .get();
        if(appDetailsOptional.isPresent()) {
           UserAppDetails details= userAppDetailsRepository.getByUserIdAndAppDetail(user.getId(), appDetailsOptional.get().getId());
           if(null!=details)
               return true;
        }
        return false;
    }

}
