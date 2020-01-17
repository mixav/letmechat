package com.damnteam.letmechat.controller;

import com.damnteam.letmechat.data.dto.UserDTO;
import com.damnteam.letmechat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

}
