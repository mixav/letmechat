package com.damnteam.letmechat.service;

import com.damnteam.letmechat.data.dao.MessageRepository;
import com.damnteam.letmechat.data.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService extends GenericService<Message> {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageRepository messageRepository;

    public Message persistMessageOutDTO(String content, String name, Long channelId) throws Exception {
        var channel = channelService.findById(channelId);
        var user = userService.findByName(name);
        if (channel.isPresent() && user.isPresent()) {
            var message = new Message();
            message.setUser(user.get());
            message.setChannel(channel.get());
            message.setMessage(content);
            return messageRepository.save(message);
        } else throw new Exception("Incorrect message received"); //TODO
    }
}
