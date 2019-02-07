package org.tomlang.livechat.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.repositories.TokenRepository;
import org.tomlang.livechat.util.TokenProvider;

import io.jsonwebtoken.Claims;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String tokenString = request.getHeader("Authorization");

        Claims claims = tokenProvider.parseToken(tokenString);
        if (!claims.getIssuer()
            .equals(TokenProvider.TOKEN_ISSUER)) {
            throw new LiveChatException("Full Authentication is required to access this resource", HttpStatus.UNAUTHORIZED);
        }

        long ttl = System.currentTimeMillis() - claims.getExpiration()
            .getTime();

        if (ttl > 30000) {
            throw new LiveChatException("Token has expired", HttpStatus.UNAUTHORIZED);
        }

        return true;

    }

}
