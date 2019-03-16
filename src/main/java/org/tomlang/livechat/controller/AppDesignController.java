package org.tomlang.livechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.AppDesignJson;
import org.tomlang.livechat.service.AppDesignService;

@RestController
public class AppDesignController {
    
    @Autowired
    AppDesignService appDesignService;

    @GetMapping("/api/secured/app/messenger/design")
    public ResponseEntity<AppDesignJson> getAppDesign(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode) throws LiveChatException {
        return ResponseEntity.ok().body(appDesignService.getDesign(appHashCode));
    }
    
    
    @PutMapping("/api/secured/app/messenger/design")
    public ResponseEntity<AppDesignJson> updateAppDesign(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode, @RequestBody AppDesignJson json) throws LiveChatException {
        return ResponseEntity.ok().body(appDesignService.updateDesign(appHashCode, json));
    }
}
