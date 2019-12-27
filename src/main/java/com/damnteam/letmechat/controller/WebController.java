package com.damnteam.letmechat.controller;

import com.damnteam.letmechat.data.GenericMessage;
import com.damnteam.letmechat.data.dto.UserDTO;
import com.damnteam.letmechat.service.UserService;
import com.damnteam.letmechat.util.GenericResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;


@Controller
public class WebController {

    private final UserService userService;

    public WebController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse registration(@Valid UserDTO userDTO) throws Exception {
        userService.createUserFromDTO(userDTO);
        return new GenericResponse("success");
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public GenericMessage genericMessage() {
        return new GenericMessage("user", "message");
    }
}
