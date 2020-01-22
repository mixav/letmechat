package com.damnteam.letmechat.controller;

import com.damnteam.letmechat.data.dto.ChannelDTO;
import com.damnteam.letmechat.data.dto.UserDTO;
import com.damnteam.letmechat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{username}")
    @ResponseBody
    public UserDTO userInfo(@PathVariable(name = "username") String username) throws Exception {
        if (userService.findByName(username).isPresent())
            return new UserDTO(userService.findByName(username).get());
        throw new Exception("No user found");
    }

    @GetMapping("subscriptions")
    @ResponseBody
    public List<ChannelDTO> userSubscriptions(Authentication authentication) throws Exception {
        var user = userService.findByName(authentication.getName());
        if (user.isPresent())
            return user.get().getSubscriptions().stream()
                    .map(channel -> ChannelDTO.getForUser(channel, user.get()))
                    .collect(Collectors.toList());
        throw new Exception("Bad user credentials");
    }
}
