package com.damnteam.letmechat.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class WebController {

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
}
