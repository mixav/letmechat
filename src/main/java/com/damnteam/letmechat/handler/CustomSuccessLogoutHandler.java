package com.damnteam.letmechat.handler;

import com.damnteam.letmechat.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomSuccessLogoutHandler implements LogoutSuccessHandler {

    @Autowired
    UserService userService;

    @SneakyThrows
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        var user = userService.findByName(authentication.getName());
        user.setOnline(false);
        userService.save(user);
        response.sendRedirect("/");
    }
}
