package com.damnteam.letmechat.controller;

import com.damnteam.letmechat.data.constants.Privilege;
import com.damnteam.letmechat.data.dto.ChannelDTO;
import com.damnteam.letmechat.service.ChannelService;
import com.damnteam.letmechat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("channel")
public class ChannelController {

    @Autowired
    ChannelService channelService;

    @Autowired
    private UserService userService;

    @PostMapping("subscribe/{channelId}")
    public void subscribe(@PathVariable(name = "channelId") Long channelId, Authentication authentication) {
        var channel = channelService.findById(channelId);
        var user = userService.findByName(authentication.getName());
        if (channel.isPresent() && user.isPresent())
            channelService.subscribe(user.get(), channel.get());
    }

    @PostMapping("unsubscribe/{channelId}")
    public void unsubscribe(@PathVariable(name = "channelId") Long channelId, Authentication authentication) {
        var channel = channelService.findById(channelId);
        var user = userService.findByName(authentication.getName());
        if (channel.isPresent() && user.isPresent())
            channelService.unsubscribe(user.get(), channel.get());
    }

    @GetMapping("list")
    @ResponseBody
    public List<ChannelDTO> getChannelList() {
        return channelService.findAll().stream().map(ChannelDTO::new).collect(Collectors.toList());
    }

    @PostMapping("addChannel")
    @Secured(Privilege.Constants.ROLE_USER)//TODO
    public void addChannel(@NotNull String name) throws Exception {
        channelService.create(name);
    }

}
