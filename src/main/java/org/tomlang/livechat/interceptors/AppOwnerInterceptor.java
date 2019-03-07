package org.tomlang.livechat.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.tomlang.livechat.entities.App;
import org.tomlang.livechat.entities.User;
import org.tomlang.livechat.entities.UserAppDetails;
import org.tomlang.livechat.enums.Role;
import org.tomlang.livechat.enums.UserStatus;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.repositories.TokenRepository;
import org.tomlang.livechat.service.AppService;
import org.tomlang.livechat.service.UserService;
import org.tomlang.livechat.util.TokenProvider;

@Component
public class AppOwnerInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    UserService userService;

    @Autowired
    AppService appService;

    @Autowired
    TokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println(request.getRequestURI());
        if (request.getRequestURI()
            .equals("/api/secured/app")) {
            return true;
        }

        String appHash = request.getHeader("X-App-Token");
        App app = appService.getAppByHashToken(appHash);
        String tokenString = request.getHeader("Authorization");
        // Token token = tokenRepository.findByToken(tokenString);
        User user = userService.getUserbyId(tokenProvider.getUserIdFromJwt(tokenString))
            .get();

        UserAppDetails details = userService.getByUserAndAppDetails(user.getId(), app.getAppDetailsId());
        if (details.getRole()
            .equals(Role.OWNER)
            || details.getRole()
                .equals(Role.ADMIN)) {
            if (null != details.getUserStatus()) {
                if (details.getUserStatus()
                    .equals(UserStatus.DEACTIVATED)
                    || details.getUserStatus()
                        .equals(UserStatus.INVITED)) {
                    return false;
                }
            }
            return true;
        } else {
            throw new LiveChatException("User Has no app with given hash", HttpStatus.UNAUTHORIZED);
        }

    }

}
