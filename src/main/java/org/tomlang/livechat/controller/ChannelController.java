package org.tomlang.livechat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.AppChannelDeleteRequest;
import org.tomlang.livechat.json.AppChannelRequest;
import org.tomlang.livechat.json.AppChannelResponseSimplified;
import org.tomlang.livechat.service.AppChannelService;

@RestController
public class ChannelController {

    @Autowired
    AppChannelService appChannelService;

    @PostMapping("/api/secured/app/channel")
    public ResponseEntity<AppChannelRequest> createChannel(@RequestBody AppChannelRequest request, @RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode) throws LiveChatException {
        appChannelService.createAppChannel(authToken, appHashCode, request);
        return ResponseEntity.ok()
            .body(request);

    }
    
    @PutMapping("/api/secured/app/channel")
    public ResponseEntity<AppChannelRequest> updateChannel(@RequestBody AppChannelRequest request, @RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode) throws LiveChatException {
        appChannelService.updateAppChannel(authToken, appHashCode, request);
        return ResponseEntity.ok()
            .body(request);

    }
    
    @DeleteMapping("/api/secured/app/channel")
    public void deleteChannel(@RequestBody AppChannelDeleteRequest request, @RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode ) throws LiveChatException {
        appChannelService.deleteChannle(authToken, appHashCode, request);
    }
    
    
    @GetMapping("/api/secured/app/channel")
    public ResponseEntity<List<AppChannelRequest>> getChannels(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode ) {
        return ResponseEntity.ok().body(appChannelService.getChannels(authToken, appHashCode));           
    }

    @GetMapping("/api/secured/app/channel/simplified")
    public ResponseEntity<List<AppChannelResponseSimplified>> getChannelsSimplified(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode ) {
        return ResponseEntity.ok().body(appChannelService.getChannelsSimplified(authToken, appHashCode));           
    }
}
