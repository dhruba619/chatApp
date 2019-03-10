package org.tomlang.livechat.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tomlang.livechat.entities.App;
import org.tomlang.livechat.entities.AppChannel;
import org.tomlang.livechat.enums.ChannelForwardPrefixConstant;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.AppChannelDeleteRequest;
import org.tomlang.livechat.json.AppChannelRequest;
import org.tomlang.livechat.json.AppChannelResponseSimplified;
import org.tomlang.livechat.json.AppTeamResponse;
import org.tomlang.livechat.repositories.AppChannelRepository;
import org.tomlang.livechat.repositories.AppRepository;
import org.tomlang.livechat.repositories.UserAppDetailsRepository;

@Service
public class AppChannelService {

    @Autowired
    private AppChannelRepository channelRepo;

    @Autowired
    private AppRepository appRepo;

    @Autowired
    private UserAppDetailsRepository userAppDetailsRepo;

    @Autowired
    private AppDetailsService appDetailService;

    /**
     * Service method to create a channel
     * @param authToken
     * @param appHash
     * @param request
     * @throws LiveChatException
     */
    public void createAppChannel(String authToken, String appHash, AppChannelRequest request) throws LiveChatException {
        // Check if the team members are a part of the app and status not invited or deactivated
        // check onlineforwards and offlineforwards same as team members
        validateMembersForChannel(appHash, appHash, request);
        App app = appRepo.findByAppHashToken(appHash);
        if (null != app) {
            // check if default is true, than find the default channel of the app, make it not default and set this as default
            if (request.isDefaultChannel()) {
                AppChannel defaultChannel = channelRepo.findDefaultChannel(app.getId());
                if (null != defaultChannel) {
                    defaultChannel.setDefaultChannel(false);
                    channelRepo.save(defaultChannel);
                }
            }
            AppChannel newChannel = new AppChannel();
            newChannel.setAppId(app.getId());

            if (request.isDefaultChannel()) {
                newChannel.setDefaultChannel(true);
            } else {
                newChannel.setDefaultChannel(false);
            }

            newChannel.setDescription(request.getDescription());
            newChannel.setMembers(request.getMembers()
                .toString());
            newChannel.setName(request.getName());
            newChannel.setOfflineForward(request.getOfflineForward());
            newChannel.setOnlineForward(request.getOnlineForward());

            newChannel = channelRepo.save(newChannel);
            request.setId(newChannel.getId());

        } else {
            throw new LiveChatException("Not a valid app", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Service method to update a channel
     * @param authToken
     * @param appHash
     * @param request
     * @throws LiveChatException
     */
    public void updateAppChannel(String authToken, String appHash, AppChannelRequest request) throws LiveChatException {
        if (null == request.getId()) {
            throw new LiveChatException("No channel found to update", HttpStatus.BAD_REQUEST);
        }

        Optional<AppChannel> updateChannelOptional = channelRepo.findById(request.getId());

        if (!updateChannelOptional.isPresent()) {
            throw new LiveChatException("No channel found to update", HttpStatus.BAD_REQUEST);
        }

        AppChannel updateChannel = updateChannelOptional.get();

        if (updateChannel.isDefaultChannel()) {
            if (!request.isDefaultChannel()) {
                throw new LiveChatException("Default to non deafault is not allowed", HttpStatus.BAD_REQUEST);
            }
        }

        validateMembersForChannel(appHash, appHash, request);
        App app = appRepo.findByAppHashToken(appHash);
        if (null != app) {
            // check if default is true, than find the default channel of the app, make it not default and set this as default
            if (request.isDefaultChannel()) {
                AppChannel defaultChannel = channelRepo.findDefaultChannel(app.getId());
                if (null != defaultChannel) {
                    defaultChannel.setDefaultChannel(false);
                    channelRepo.save(defaultChannel);
                }
            }

            updateChannel.setAppId(app.getId());

            if (request.isDefaultChannel()) {
                updateChannel.setDefaultChannel(true);
            } else {
                updateChannel.setDefaultChannel(false);
            }

            updateChannel.setDescription(request.getDescription());
            updateChannel.setMembers(request.getMembers()
                .toString());
            updateChannel.setName(request.getName());
            updateChannel.setOfflineForward(request.getOfflineForward());
            updateChannel.setOnlineForward(request.getOnlineForward());
            updateChannel = channelRepo.save(updateChannel);
            request.setId(updateChannel.getId());

        } else {
            throw new LiveChatException("Not a valid app", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Service method to delete the channel
     * @param authToken
     * @param appHash
     * @param request
     * @throws LiveChatException
     */
    public void deleteChannle(String authToken, String appHash, AppChannelDeleteRequest request) throws LiveChatException {

        if (null == request.getId()) {
            throw new LiveChatException("No Id passed to delete the channel", HttpStatus.BAD_REQUEST);
        }
        Optional<AppChannel> appChannelOptional = channelRepo.findById(request.getId());

        if (appChannelOptional.isPresent()) {
            AppChannel appChannel = appChannelOptional.get();
            if (appChannel.isDefaultChannel()) {
                throw new LiveChatException("Cannot delete the default channel", HttpStatus.BAD_REQUEST);
            } else {
                channelRepo.deleteById(request.getId());
            }
        } else {
            throw new LiveChatException("No Channel found", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 
     * @param authToken
     * @param appHash
     * @return
     */
    public List<AppChannelRequest> getChannels(String authToken, String appHash) {
        App app = appRepo.findByAppHashToken(appHash);
        List<AppChannel> appChannels = channelRepo.findAllForApp(app.getId());
        List<AppChannelRequest> response = new ArrayList<>();
        
        for(AppChannel appChannel: appChannels) {
            AppChannelRequest request = new AppChannelRequest();
            request.setDefaultChannel(appChannel.isDefaultChannel());
            request.setDescription(appChannel.getDescription());
            request.setId(appChannel.getId());
            
            List<String> mems = Arrays.asList(appChannel.getMembers().split(","));
            request.setMembers(mems);
            request.setName(appChannel.getName());
            request.setOfflineForward(appChannel.getOfflineForward());
            request.setOnlineForward(appChannel.getOnlineForward());
            response.add(request);
            
        }
        
        return response;

    }

    /**
     * 
     * @param authToken
     * @param appHash
     * @return
     */
    public List<AppChannelResponseSimplified> getChannelsSimplified(String authToken, String appHash) {
        App app = appRepo.findByAppHashToken(appHash);
        List<AppChannel> appChannels = channelRepo.findAllForApp(app.getId());
        List<AppChannelResponseSimplified> response = new ArrayList<>();
        
        for(AppChannel appChannel: appChannels) {
            AppChannelResponseSimplified request = new AppChannelResponseSimplified();
         
            request.setId(appChannel.getId());
            request.setName(appChannel.getName());
            
            response.add(request);
            
        }
        
        return response;
    }

    /**
     * 
     * @param authToken
     * @param appHash
     * @param request
     * @throws LiveChatException
     */
    private void validateMembersForChannel(String authToken, String appHash, AppChannelRequest request) throws LiveChatException {
        List<AppTeamResponse> teamMembers = appDetailService.getAppTeamMembers(authToken, appHash);
        List<Integer> teamUidList = new ArrayList<>();
        List<Integer> teamUidListInvlid = new ArrayList<>();
        for (AppTeamResponse appTeam : teamMembers) {
            teamUidList.add(appTeam.getId());
            if ("INVITED".equals(appTeam.getStatus()) || "DEACTIVATED".equals(appTeam.getStatus())) {
                teamUidListInvlid.add(appTeam.getId());
            }

        }

        if (null != request.getOfflineForward() && request.getOfflineForward()
            .startsWith(ChannelForwardPrefixConstant.TEAM_MEMER)) {
            Integer fuid = Integer.valueOf(request.getOfflineForward()
                .substring(1));
            if (!teamUidList.contains(fuid)) {
                throw new LiveChatException("Offline Forward is not a Team Member", HttpStatus.BAD_REQUEST);
            }
            if (teamUidListInvlid.contains(fuid)) {
                throw new LiveChatException("Team Member is either Deactivated or invited", HttpStatus.BAD_REQUEST);
            }
        }

        if (null != request.getOnlineForward() && request.getOnlineForward()
            .startsWith(ChannelForwardPrefixConstant.TEAM_MEMER)) {
            Integer nuid = Integer.valueOf(request.getOnlineForward()
                .substring(1));
            if (!teamUidList.contains(nuid)) {
                throw new LiveChatException("Online Forward is not a Team Member", HttpStatus.BAD_REQUEST);
            }
            if (teamUidListInvlid.contains(nuid)) {
                throw new LiveChatException("Team Member is either Deactivated or invited", HttpStatus.BAD_REQUEST);
            }
        }

        if (null != request.getMembers() && request.getMembers()
            .size() > 0) {
            for (String mem : request.getMembers()) {
                if (!teamUidList.contains(Integer.valueOf(mem.substring(1)))) {
                    throw new LiveChatException("One or more members doesnot belong to the app team", HttpStatus.BAD_REQUEST);
                }
                if (teamUidListInvlid.contains(Integer.valueOf(mem.substring(1)))) {
                    throw new LiveChatException("Team Member is either Deactivated or invited", HttpStatus.BAD_REQUEST);
                }
            }
        }

    }

}
