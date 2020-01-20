package com.damnteam.letmechat.handler;

import com.damnteam.letmechat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomSuccessLogoutHandler implements LogoutSuccessHandler {

    @Autowired
    UserService userService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var temp = userService.findByName(authentication.getName());
        temp.ifPresent(user -> {
            user.setOnline(false);
            userService.save(user);
        });
        response.sendRedirect("/");
    }
}
