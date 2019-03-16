package org.tomlang.livechat.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tomlang.livechat.entities.App;
import org.tomlang.livechat.entities.AppDetails;
import org.tomlang.livechat.entities.User;
import org.tomlang.livechat.entities.UserAppDetails;
import org.tomlang.livechat.enums.ChannelForwardPrefixConstant;
import org.tomlang.livechat.enums.Role;
import org.tomlang.livechat.enums.TagTarget;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.AppChannelRequest;
import org.tomlang.livechat.json.AppRequest;
import org.tomlang.livechat.json.AppTagJson;
import org.tomlang.livechat.repositories.AppDetailsRepository;
import org.tomlang.livechat.repositories.AppRepository;
import org.tomlang.livechat.repositories.TokenRepository;
import org.tomlang.livechat.repositories.UserAppDetailsRepository;
import org.tomlang.livechat.util.TimeZoneUtil;
import org.tomlang.livechat.util.TokenProvider;

@Service
public class AppService {

    Logger logger = LoggerFactory.getLogger(AppService.class);

    @Autowired
    AppRepository appRepository;

    @Autowired
    AppDetailsRepository appDetailsRepository;

    @Autowired
    UserAppDetailsRepository userAppDetailsRepository;

    @Autowired
    UserService userService;

    @Autowired
    TimeZoneUtil timeZoneUtil;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    TokenProvider tokenProvider;
    
    @Autowired
    AppChannelService appChannelService;
    
    
    @Autowired
    AppTagService appTagService;

    public App createApp(AppRequest request, String authToken) throws NoSuchAlgorithmException, LiveChatException {
        // find user from token
        // Token token = tokenRepository.findByToken(request.getUser_token());
        User user = userService.getUserbyId(tokenProvider.getUserIdFromJwt(authToken))
            .get();

        // create app hash code
        String appHash = createAppHash();

        // create and save appdetails
        AppDetails details = new AppDetails();
        details.setName(request.getName());
        ;
        if (timeZoneUtil.checkTimeZone(request.getTimeZone())) {
            details.setTimeZone(request.getTimeZone());
        } else {
            details.setTimeZone(timeZoneUtil.getDefault());
        }

        details = appDetailsRepository.save(details);

        // create and save app
        App app = new App();
        app.setAppDetailsId(details.getId());
        app.setAppHashcode(appHash);
        app.setCreatedBy(user.getId());
        app = appRepository.save(app);

        // create and save user app details

        UserAppDetails userAppDetails = new UserAppDetails();
        userAppDetails.setAppDetailsId(details.getId());
        userAppDetails.setUserId(user.getId());
        userAppDetails.setRole(Role.OWNER);

        userAppDetailsRepository.save(userAppDetails);
        
        // Need to create the deafault channel for the app here
        AppChannelRequest appChannelRequest = new AppChannelRequest();
        appChannelRequest.setDefaultChannel(true);
        appChannelRequest.setDescription("This is the default channel.");
        appChannelRequest.setName("Default Channel");
        List<String> mems = new ArrayList<>();
        mems.add(ChannelForwardPrefixConstant.TEAM_MEMER+String.valueOf(user.getId()));
        appChannelRequest.setMembers(mems);
        appChannelService.createAppChannel(authToken, app.getAppHashcode(), appChannelRequest);
        
        //need to create the default tags here
        AppTagJson json1 =  new AppTagJson();
        json1.setColor("#3b7587");
        json1.setName("Open");
        json1.setTarget(TagTarget.CONVERSATION);
        
        AppTagJson json2 =  new AppTagJson();
        json2.setColor("#e3ff00");
        json2.setName("New Lead");
        json2.setTarget(TagTarget.CONTACT);
        appTagService.createTag(authToken, appHash, json1);
        appTagService.createTag(authToken, appHash, json2);
        
        

        return app;

    }

    public App getAppByHashToken(String token) throws LiveChatException {
        logger.info("Searching app by app hash: " + token);

        App app = appRepository.findByAppHashToken(token);
        if (null != app) {
            return app;
        } else {
            throw new LiveChatException("No App found with given hash", HttpStatus.NOT_FOUND);
        }

    }

    private String createAppHash() throws NoSuchAlgorithmException {

        String input = UUID.randomUUID()
            .toString();
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);

        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        hashtext = hashtext.substring(0, 8);
        return hashtext;
    }

}
