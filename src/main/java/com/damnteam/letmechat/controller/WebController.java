package com.damnteam.letmechat.controller;

import com.damnteam.letmechat.data.constants.Privilege;
import com.damnteam.letmechat.data.dto.ChannelDTO;
import com.damnteam.letmechat.data.dto.GenericMessage;
import com.damnteam.letmechat.data.dto.UserDTO;
import com.damnteam.letmechat.service.ChannelService;
import com.damnteam.letmechat.service.MessageService;
import com.damnteam.letmechat.service.UserService;
import com.damnteam.letmechat.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class WebController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse registration(@Valid UserDTO userDTO) throws Exception {
        userService.createUserFromDTO(userDTO);
        return new GenericResponse("success");
    }

    @MessageMapping("/receiver/{channelId}")
    @SendTo("/chat/{channelId}")
    public GenericMessage genericMessage(GenericMessage message, Authentication auth, @DestinationVariable Long channelId) throws Exception {
        var savedMessage = messageService.persistMessageOutDTO(message.getMessage(), auth.getName(), channelId);
        return new GenericMessage(savedMessage);
    }

    @GetMapping("api/channels")
    @ResponseBody
    public List<ChannelDTO> channels(Authentication authentication) {
        //TODO
        return channelService.findAll().stream().map(ChannelDTO::new).collect(Collectors.toList());
    }

    @PostMapping("addChannel")
    @Secured(Privilege.Constants.ROLE_USER)//TODO think about it
    public void addChannel(@NotNull String name) throws Exception {
        channelService.create(name);
    }

    @GetMapping("api/last/{channelId}")
    @ResponseBody
    public List<GenericMessage> lastMessages(@PathVariable Long channelId) throws Exception {
        return messageService.findLastInChannel(channelId).stream().map(GenericMessage::new).collect(Collectors.toList());
    }

}
