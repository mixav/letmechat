package com.damnteam.letmechat.handler;

import com.damnteam.letmechat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomSuccessAuthenticationHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        var temp = userService.findByName(authentication.getName());
        temp.ifPresent(user -> {
            user.setOnline(true);
            userService.save(user);
        });
        response.sendRedirect("/");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var temp = userService.findByName(authentication.getName());
        temp.ifPresent(user -> {
            user.setOnline(true);
            userService.save(user);
        });
        response.sendRedirect("/");
    }
}
