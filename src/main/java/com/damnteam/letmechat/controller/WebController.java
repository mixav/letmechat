package com.damnteam.letmechat.controller;

import com.damnteam.letmechat.data.dao.ChannelRepository;
import com.damnteam.letmechat.data.dto.ChannelDTO;
import com.damnteam.letmechat.data.dto.GenericMessage;
import com.damnteam.letmechat.data.dto.UserDTO;
import com.damnteam.letmechat.service.MessageService;
import com.damnteam.letmechat.service.UserService;
import com.damnteam.letmechat.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class WebController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChannelRepository channelRepository;

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
        messageService.persistMessageOutDTO(message.getMessage(), auth.getName(), channelId);
        return new GenericMessage(auth.getName(), message.getMessage());
    }

    @GetMapping("api/channels")
    @ResponseBody
    public List<ChannelDTO> channels(Authentication authentication) {
        //TODO
        return channelRepository.findAll().stream().map(ChannelDTO::new).collect(Collectors.toList());
    }
}
