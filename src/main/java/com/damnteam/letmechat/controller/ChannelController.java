package com.damnteam.letmechat.controller;

import com.damnteam.letmechat.data.constants.Privilege;
import com.damnteam.letmechat.data.dto.ChannelDTO;
import com.damnteam.letmechat.data.dto.UserDTO;
import com.damnteam.letmechat.service.ChannelService;
import com.damnteam.letmechat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @ResponseBody
    public ChannelDTO subscribe(@PathVariable(name = "channelId") Long channelId, Authentication authentication) throws Exception {
        var channel = channelService.findById(channelId);
        var user = userService.findByName(authentication.getName());
        if (channel.isPresent()) {
            return ChannelDTO.getForUser(channelService.subscribe(user, channel.get()), user);
        }
        throw new Exception("Wrong credentials");
    }

    @PostMapping("unsubscribe/{channelId}")
    @ResponseBody
    public ChannelDTO unsubscribe(@PathVariable(name = "channelId") Long channelId, Authentication authentication) throws Exception {
        var channel = channelService.findById(channelId);
        var user = userService.findByName(authentication.getName());
        if (channel.isPresent())
            return ChannelDTO.getForUser(channelService.unsubscribe(user, channel.get()), user);
        throw new Exception("Wrong credentials"); //TODO
    }

    @GetMapping("list")
    @ResponseBody
    public List<ChannelDTO> getChannelList(Authentication authentication) throws Exception {
        var user = userService.findByName(authentication.getName());
        return channelService.findAll().stream()
                .map(channel -> ChannelDTO.getForUser(channel, user))
                .collect(Collectors.toList());
    }

    @PostMapping("create")
    @Secured(Privilege.Constants.ROLE_USER)//TODO
    @ResponseBody
    public ChannelDTO addChannel(@RequestParam String name) throws Exception {
        if (name.isEmpty()) throw new Exception("Empty channel name");
        return new ChannelDTO(channelService.create(name));
    }

    @GetMapping("subscribers/{channelId}")
    @ResponseBody
    public List<UserDTO> subscribers(@PathVariable(name = "channelId") Long channelId) throws Exception {

        return channelService.getSubscribers(channelId).stream()
                .map(user -> {
                    var dto = new UserDTO(user);
                    dto.setOnline(userService.checkOnline(user));
                    return dto;
                }).collect(Collectors.toList());
    }
}
