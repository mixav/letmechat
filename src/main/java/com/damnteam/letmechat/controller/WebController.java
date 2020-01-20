package com.damnteam.letmechat.controller;

import com.damnteam.letmechat.data.constants.Privilege;
import com.damnteam.letmechat.data.dto.ChannelDTO;
import com.damnteam.letmechat.data.dto.GenericMessage;
import com.damnteam.letmechat.data.dto.RegistrationDTO;
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
    public GenericResponse registration(@Valid RegistrationDTO registrationDTO) throws Exception {
        userService.createUserFromDTO(registrationDTO);
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
    public List<ChannelDTO> channels(Authentication authentication) throws Exception {
        var user = userService.findByName(authentication.getName());
        if (user.isPresent())
            return user.get().getSubscriptions().stream().map(ChannelDTO::new).collect(Collectors.toList());
        throw new Exception("Bad user credentials");
    }

    @GetMapping("api/last/{channelId}")
    @ResponseBody
    public List<GenericMessage> lastMessages(@PathVariable Long channelId) throws Exception {
        //TODO availability only to channel subscribers
        return messageService.findLastInChannel(channelId).stream().map(GenericMessage::new).collect(Collectors.toList());
    }

    @GetMapping("api/prev/{channelId}")
    @ResponseBody
    public List<GenericMessage> prevMessages(@PathVariable Long channelId, @RequestParam("id") Long fromId) throws Exception {
        //TODO availability only to channel subscribers
        return messageService.findPrevInChannel(channelId, fromId).stream().map(GenericMessage::new).collect(Collectors.toList());
    }

}
