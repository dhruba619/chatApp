package org.tomlang.livechat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.ActivateDeactivateUninviteTeamRequest;
import org.tomlang.livechat.json.AppTeamResponse;
import org.tomlang.livechat.json.AppTeamResponseSimplified;
import org.tomlang.livechat.json.CreateTeamRequest;
import org.tomlang.livechat.json.UpdateTeamMember;
import org.tomlang.livechat.service.AppDetailsService;
import org.tomlang.livechat.service.AppService;
import org.tomlang.livechat.service.AppTeamService;
import org.tomlang.livechat.service.FileStorageService;
import org.tomlang.livechat.service.UserAppService;

public class TeamController {

    @Autowired
    AppService appService;

    @Autowired
    AppDetailsService appDetailService;

    @Autowired
    UserAppService userAppService;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    AppTeamService appTeamService;

    @GetMapping("/api/secured/app/team")
    public ResponseEntity<List<AppTeamResponse>> getTeam(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode, @RequestParam String status) {

        return ResponseEntity.ok()
            .body(appDetailService.getAppTeamMembers(authToken, appHashCode, status));
    }

    @PostMapping("/api/secured/app/team")
    public ResponseEntity<AppTeamResponse> createTeam(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode, @RequestBody CreateTeamRequest request) throws LiveChatException {

        AppTeamResponse response = new AppTeamResponse();
        appTeamService.proccessCreateTeam(authToken, appHashCode, request, response);
        return ResponseEntity.ok()
            .body(response);
    }

    @PutMapping("/api/secured/app/team/deactivate")
    @ResponseStatus(HttpStatus.OK)
    public void deactivateTeamMember(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode, @RequestBody ActivateDeactivateUninviteTeamRequest request) throws LiveChatException {
        appTeamService.activateDeactivateTeamMember(authToken, appHashCode, request, false);
    }

    @PutMapping("/api/secured/app/team/reactivate")
    @ResponseStatus(HttpStatus.OK)
    public void reactivateTeamMember(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode, @RequestBody ActivateDeactivateUninviteTeamRequest request) throws LiveChatException {
        appTeamService.activateDeactivateTeamMember(authToken, appHashCode, request, true);
    }

    @PutMapping("/api/secured/app/team")
    public ResponseEntity<UpdateTeamMember> updateTeamMember(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode, @RequestBody UpdateTeamMember request) throws LiveChatException {
        return ResponseEntity.ok().body(appTeamService.updateTeamMember(authToken, appHashCode, request));
    }

    @PutMapping("/api/secured/app/team/uninvite")
    @ResponseStatus(HttpStatus.OK)
    public void uninviteTeamMember(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode, @RequestBody ActivateDeactivateUninviteTeamRequest request) throws LiveChatException {
        appTeamService.uninviteTeamMember(authToken, appHashCode, request);
    }

    @GetMapping("/api/secured/app/team/simplified")
    public ResponseEntity<List<AppTeamResponseSimplified>> getTeamSimplified(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode) {
        return ResponseEntity.ok().body(appTeamService.getAppTeamMembersSimlified(authToken, appHashCode));

    }
}
