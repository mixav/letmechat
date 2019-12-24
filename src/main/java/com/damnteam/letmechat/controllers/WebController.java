package com.damnteam.letmechat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {
    @RequestMapping(value="/registration", method = RequestMethod.POST)
    @ResponseBody
    public String registration(Object userDataFromJs) throws Exception {
        return "signUp";
    }
}


