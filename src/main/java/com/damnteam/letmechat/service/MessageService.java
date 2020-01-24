package com.damnteam.letmechat.service;

import com.damnteam.letmechat.data.dao.MessageRepository;
import com.damnteam.letmechat.data.model.Channel;
import com.damnteam.letmechat.data.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService extends GenericService<Message> {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageRepository messageRepository;

    @Transactional
    public Message persistMessageOutDTO(String content, String username, Long channelId) throws Exception {
        var channel = channelService.findById(channelId);
        var user = userService.findByName(username);
        var message = new Message();
        message.setUser(user);
        message.setChannel(channel);
        message.setMessage(content);
        message.setTime(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> findLastInChannel(Long channelId) throws Exception {
        var channel = channelService.findById(channelId);
        if (isSubscriber(channel))
            return messageRepository.findTop30ByChannelOrderByTimeDesc(channel);
        throw new Exception("User is not subscriber of the channel");//TODO
    }

    public List<Message> findPrevInChannel(Long channelId, Long fromId) throws Exception {
        //TODO page size flexibility for better api experience
        var channel = channelService.findById(channelId);
        if (isSubscriber(channel))
            return messageRepository.findByIdLessThanAndChannelOrderByIdDesc(fromId, channel, PageRequest.of(0, 50));
        throw new Exception("User is not subscriber of the channel");//TODO
    }

    public boolean isSubscriber(Channel channel) throws Exception {
        var user = userService.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
        return channel.getSubscribers().contains(user);
    }
}
