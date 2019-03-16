package org.tomlang.livechat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tomlang.livechat.entities.App;
import org.tomlang.livechat.entities.AppDetails;
import org.tomlang.livechat.entities.User;
import org.tomlang.livechat.entities.UserAppDetails;
import org.tomlang.livechat.entities.UserAppInvitedDetails;
import org.tomlang.livechat.enums.UserStatus;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.AppSpecificInfo;
import org.tomlang.livechat.json.AppTeamResponse;
import org.tomlang.livechat.json.SocialLinks;
import org.tomlang.livechat.json.UserAppDetailsResponse;
import org.tomlang.livechat.repositories.AppDetailsRepository;
import org.tomlang.livechat.repositories.AppRepository;
import org.tomlang.livechat.repositories.UserAppDetailsInvitedRepository;
import org.tomlang.livechat.repositories.UserAppDetailsRepository;
import org.tomlang.livechat.util.TimeZoneUtil;
import org.tomlang.livechat.util.TokenProvider;

import com.google.gson.Gson;

@Service
public class AppDetailsService {

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

    public List<UserAppDetailsResponse> getAppDetailsByUser(int userId) {
        List<UserAppDetailsResponse> response = new ArrayList<>();

        List<UserAppDetails> details = userAppDetailsRepository.getByUserIdAndAppDetail(userId);
        for (UserAppDetails detail : details) {
            UserAppDetailsResponse res = new UserAppDetailsResponse();
            // Get appdetails
            AppDetails appDetail = appDetailsRepository.findById(detail.getAppDetailsId())
                .get();
            App app = appRepository.findByAppDetailId(appDetail.getId());
            res.setDescription(appDetail.getDescription());
            res.setHash(app.getAppHashcode());
            res.setId(app.getId());
            res.setName(appDetail.getName());
            res.setOwnedBy(app.getCreatedBy());
            res.setTimeZone(appDetail.getTimeZone());
            res.setSocialLinks(new Gson().fromJson(appDetail.getSocialLinks(), SocialLinks.class));
            response.add(res);
        }
        return response;
    }

    public void updateAppDetails(String authToken, String appHashCode, AppSpecificInfo info) throws LiveChatException {
        App app = appService.getAppByHashToken(appHashCode);
        Optional<AppDetails> appDetailsOptional = appDetailsRepository.findById(app.getAppDetailsId());

        AppDetails appDetails = appDetailsOptional.get();

        appDetails.setDescription(info.getDescription());
        appDetails.setName(info.getName());
        appDetails.setSocialLinks(new Gson().toJson(info.getSocialLinks()));
        if (timeZoneUtil.checkTimeZone(info.getTimeZone())) {
            appDetails.setTimeZone(info.getTimeZone());
        } else {
            appDetails.setTimeZone(timeZoneUtil.getDefault());
        }

        appDetailsRepository.save(appDetails);
    }

    public void updateAvatar(String fileName, String authToken, String appHashCode) throws LiveChatException {
        App app = appService.getAppByHashToken(appHashCode);
        Optional<AppDetails> appDetailsOptional = appDetailsRepository.findById(app.getAppDetailsId());

        AppDetails appDetails = appDetailsOptional.get();
        appDetails.setImage(fileName);

        appDetailsRepository.save(appDetails);

    }

    public List<AppTeamResponse> getAppTeamMembers(String authToken, String appHashCode, String status) {
        // find app by hashcode
        // get all UserAppDetails where appdetails id from the found app
        List<AppTeamResponse> reponses = new ArrayList<>();

        App app = appRepository.findByAppHashToken(appHashCode);
        List<UserAppDetails> userAppDetailsList = userAppDetailsRepository.getByAppDetailId(app.getAppDetailsId());

        UserStatus filterStatus = null;
        if (null != status) {
            filterStatus = UserStatus.valueOf(status.toUpperCase());
        }

        for (UserAppDetails detail : userAppDetailsList) {

            User user = null;
            if (null != detail.getUserId())
                user = userService.getUserbyId(detail.getUserId())
                    .get();
            AppTeamResponse response = new AppTeamResponse();
            response.setChatTitle(detail.getChatTitle());
            response.setId(detail.getUserId());
            response.setRole(detail.getRole()
                .toString());
            if (null != detail.getUserStatus())
                response.setStatus(detail.getUserStatus()
                    .toString());

            if (null != detail.getUserStatus() && detail.getUserStatus()
                .equals(UserStatus.INVITED)) {
                if (null != user) {
                    response.setEmail(user.getEmail());
                    response.setImage(user.getImage());
                    response.setName(user.getFullName());
                } else {
                    UserAppInvitedDetails invd = userAppDetailsInvitedRepository.findById(detail.getInvitedDetailsId())
                        .get();
                    if (null != invd) {
                        response.setEmail(invd.getEmail());
                        response.setName(invd.getName());
                    }
                }
            } else {
                response.setEmail(user.getEmail());
                response.setImage(user.getImage());
                response.setName(user.getFullName());
            }

            if (null != filterStatus) {
                if (filterStatus.equals(detail.getUserStatus())) {
                    reponses.add(response);
                }
            } else {
                reponses.add(response);
            }
        }
        return reponses;
    }
}
