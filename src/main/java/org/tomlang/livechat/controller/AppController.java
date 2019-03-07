package org.tomlang.livechat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.tomlang.livechat.entities.App;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.AppRequest;
import org.tomlang.livechat.json.AppSpecificInfo;
import org.tomlang.livechat.json.AppTeamResponse;
import org.tomlang.livechat.json.AvatarUploadResponse;
import org.tomlang.livechat.json.CreateTeamRequest;
import org.tomlang.livechat.json.IsAwayPayload;
import org.tomlang.livechat.json.UserPrivateInformation;
import org.tomlang.livechat.service.AppDetailsService;
import org.tomlang.livechat.service.AppService;
import org.tomlang.livechat.service.AppTeamService;
import org.tomlang.livechat.service.FileStorageService;
import org.tomlang.livechat.service.UserAppService;

@RestController
@RequestMapping(path = "/api/secured")
public class AppController {

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

    @RequestMapping(path = "/app", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<App> create(@RequestBody AppRequest request, @RequestHeader("Authorization") String authToken) throws Exception {
        try {
            App app = appService.createApp(request, authToken);
            return ResponseEntity.ok()
                .body(app);
        } catch (Exception e) {
            if (e instanceof LiveChatException) {
                throw e;
            } else {
                throw new LiveChatException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @RequestMapping(path = "/app/user/private/ping", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void pingFromUser(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode) {

        userAppService.processUserPing(authToken, appHashCode);
        return;
    }

    @RequestMapping(path = "/app/user/private/away", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<IsAwayPayload> updateAwayStatus(@RequestBody IsAwayPayload payload, @RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode) throws LiveChatException {
        try {
            userAppService.updateAwayStatus(payload, authToken, appHashCode);
            return ResponseEntity.ok()
                .body(payload);
        } catch (Exception e) {
            if (e instanceof LiveChatException) {
                throw e;
            } else {
                throw new LiveChatException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @RequestMapping(path = "/app/user/private/information", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<UserPrivateInformation> getAppUserInformation(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode) throws LiveChatException {
        try {
            UserPrivateInformation info = userAppService.getUserPrivateInformationForApp(authToken, appHashCode);
            return ResponseEntity.ok()
                .body(info);
        } catch (Exception e) {
            if (e instanceof LiveChatException) {
                throw e;
            } else {
                throw new LiveChatException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @RequestMapping(path = "/app/information", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<AppSpecificInfo> getAppInformation(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode) throws LiveChatException {
        try {
            AppSpecificInfo info = userAppService.getInformationForApp(authToken, appHashCode);
            return ResponseEntity.ok()
                .body(info);
        } catch (Exception e) {
            if (e instanceof LiveChatException) {
                throw e;
            } else {
                throw new LiveChatException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @RequestMapping(path = "/app/information", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<AppSpecificInfo> putAppInformation(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode, @RequestBody AppSpecificInfo appInfo) throws LiveChatException {
        try {
            appDetailService.updateAppDetails(authToken, appHashCode, appInfo);
            return ResponseEntity.ok()
                .body(appInfo);
        } catch (Exception e) {
            if (e instanceof LiveChatException) {
                throw e;
            } else {
                throw new LiveChatException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @PutMapping("/app/upload_avatar")
    public AvatarUploadResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode) throws LiveChatException {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/download/")
            .path(fileName)
            .toUriString();
        appDetailService.updateAvatar(fileDownloadUri, authToken, appHashCode);
        return new AvatarUploadResponse("success", fileDownloadUri);
    }
    
    @GetMapping("/app/team")
    public ResponseEntity<List<AppTeamResponse>> getTeam(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode) {
        
        return ResponseEntity.ok()
            .body(appDetailService.getAppTeamMembers(authToken, appHashCode));
    }
    
    @PostMapping("/app/team")
    public ResponseEntity<AppTeamResponse> createTeam(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode, @RequestBody CreateTeamRequest request) throws LiveChatException {
         
        AppTeamResponse response = new AppTeamResponse();
        appTeamService.proccessCreateTeam(authToken, appHashCode, request, response);
        return ResponseEntity.ok()
            .body(response);
    }

}
