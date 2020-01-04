package com.damnteam.letmechat.controller;

import com.damnteam.letmechat.data.GenericMessage;
import com.damnteam.letmechat.data.dao.ChannelRepository;
import com.damnteam.letmechat.data.dto.ChannelDTO;
import com.damnteam.letmechat.data.dto.UserDTO;
import com.damnteam.letmechat.service.UserService;
import com.damnteam.letmechat.util.GenericResponse;
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

    private final UserService userService;

    private final ChannelRepository channelRepository;

    public WebController(UserService userService, ChannelRepository channelRepository) {
        this.userService = userService;
        this.channelRepository = channelRepository;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse registration(@Valid UserDTO userDTO) throws Exception {
        userService.createUserFromDTO(userDTO);
        return new GenericResponse("success");
    }

    @MessageMapping("/receiver/{id}")
    @SendTo("/chat/{id}")
    public GenericMessage genericMessage(GenericMessage genericMessage, Authentication authentication, @DestinationVariable Long id) {
        return new GenericMessage(authentication.getName(), genericMessage.getMessage());
    }

    @GetMapping("api/channels")
    @ResponseBody
    public List<ChannelDTO> channels(Authentication authentication) {

        return channelRepository.findAll().stream().map(ChannelDTO::new).collect(Collectors.toList());
    }
}
