package org.tomlang.livechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tomlang.livechat.entities.Token;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.LoginRequest;
import org.tomlang.livechat.json.TokenRefreshRequest;
import org.tomlang.livechat.service.LoginService;

@RestController
public class LoginController {

    
    @Autowired
    LoginService loginService;

    @RequestMapping(path = "/api/user/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Token> login(@RequestBody LoginRequest request) throws Exception {
        try {
            Token token = loginService.generateToken(request.getEmail(), request.getPassword());
            return ResponseEntity.ok()
                .body(token);
        } catch (Exception e) {
            if (e instanceof LiveChatException) {
                throw e;
            } else {
                throw new LiveChatException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
    
    @RequestMapping(path = "/api/user/login/refresh", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Token> refresh(@RequestBody TokenRefreshRequest request) throws Exception {
        try {
            Token token = loginService.refreshAccessToken(request.getRefreshToken());
            return ResponseEntity.ok()
                .body(token);
        } catch (Exception e) {
            if (e instanceof LiveChatException) {
                throw e;
            } else {
                throw new LiveChatException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
