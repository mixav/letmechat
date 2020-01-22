package com.damnteam.letmechat.controller;

import com.damnteam.letmechat.data.dto.GenericMessage;
import com.damnteam.letmechat.data.dto.RegistrationDTO;
import com.damnteam.letmechat.service.MessageService;
import com.damnteam.letmechat.service.UserService;
import com.damnteam.letmechat.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;


@Controller
public class WebController {

    @Autowired
    private UserService userService;

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
}
