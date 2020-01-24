package com.damnteam.letmechat.controller;

import com.damnteam.letmechat.data.dto.GenericMessage;
import com.damnteam.letmechat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("last/{channelId}")
    @ResponseBody
    public List<GenericMessage> lastMessages(@PathVariable Long channelId, Authentication authentication) throws Exception {
        return messageService.findLastInChannel(channelId).stream().map(GenericMessage::new).collect(Collectors.toList());
    }

    @GetMapping("prev/{channelId}")
    @ResponseBody
    public List<GenericMessage> prevMessages(@PathVariable Long channelId, @RequestParam("id") Long fromId) throws Exception {
        return messageService.findPrevInChannel(channelId, fromId).stream().map(GenericMessage::new).collect(Collectors.toList());
    }

}
