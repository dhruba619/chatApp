package org.tomlang.livechat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.AppTagJson;
import org.tomlang.livechat.service.AppTagService;

@RestController
public class TagController {

    @Autowired
    AppTagService appTagService;

    @PostMapping("/api/secured/app/tag")
    public ResponseEntity<AppTagJson> createTag(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode, @RequestBody AppTagJson appTagJson) {

        return ResponseEntity.ok()
            .body(appTagService.createTag(authToken, appHashCode, appTagJson));
    }

    @PutMapping("/api/secured/app/tag")
    public ResponseEntity<AppTagJson> updateTag(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode, @RequestBody AppTagJson appTagJson) throws LiveChatException {

        return ResponseEntity.ok()
            .body(appTagService.updateTag(authToken, appHashCode, appTagJson));

    }

    @GetMapping("/api/secured/app/tag")
    public ResponseEntity<List<AppTagJson>> getTags(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode) throws LiveChatException {

        return ResponseEntity.ok()
            .body(appTagService.getTags(authToken, appHashCode));
    }

    @DeleteMapping("/api/secured/app/tag")
    public ResponseEntity<AppTagJson> deleteTag(@RequestHeader("Authorization") String authToken, @RequestHeader("X-App-Token") String appHashCode, @RequestBody AppTagJson appTagJson) throws LiveChatException {

        return ResponseEntity.ok()
            .body(appTagService.deleteTag(authToken, appHashCode, appTagJson));
    }

}
