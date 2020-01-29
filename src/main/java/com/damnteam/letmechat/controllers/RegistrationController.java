package com.damnteam.letmechat.controllers;


import com.damnteam.letmechat.data.dto.UserData;
import com.damnteam.letmechat.data.dao.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/registration")
public class RegistrationController {

    PasswordEncoder passwordEncoder;


    UserRepository userRepository;


    public RegistrationController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    @GetMapping
    public String registration() {
        return "signUp";
    }


    @PostMapping
    public String processRegister(UserData userData) {
        userRepository.save(userData.toUser(passwordEncoder));
        return "redirect:/login";
    }
}
