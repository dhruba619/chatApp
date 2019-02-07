package org.tomlang.livechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.tomlang.livechat.entities.App;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.AppRequest;
import org.tomlang.livechat.json.AppSpecificInfo;
import org.tomlang.livechat.json.IsAwayPayload;
import org.tomlang.livechat.json.UserPrivateInformation;
import org.tomlang.livechat.service.AppService;
import org.tomlang.livechat.service.UserAppService;

@RestController
@RequestMapping(path = "/api/secured")
public class AppController {

    @Autowired
    AppService appService;

    @Autowired
    UserAppService userAppService;

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

    @RequestMapping(path = "/app/user/ping", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void pingFromUser(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode) {

        userAppService.processUserPing(authToken, appHashCode);
        return;
    }

    @RequestMapping(path = "/app/user/away", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

}
