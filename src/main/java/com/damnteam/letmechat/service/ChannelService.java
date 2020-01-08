package com.damnteam.letmechat.service;

import com.damnteam.letmechat.data.dao.ChannelRepository;
import com.damnteam.letmechat.data.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService extends GenericService<Channel> {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserService userService;

    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    public Channel create(String name) throws Exception {
        var channel = new Channel();
        var user = userService.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user.isPresent()) {
            channel.setCreator(user.get());
            channel.setOwner(user.get());
            return save(channel);
        }
        else throw new Exception("Wrong user credentials.");
    }
}
