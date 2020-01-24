package com.damnteam.letmechat.service;

import com.damnteam.letmechat.data.dao.ChannelRepository;
import com.damnteam.letmechat.data.model.Channel;
import com.damnteam.letmechat.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
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
        if (channelRepository.findByName(name).isPresent())
            throw new Exception("Channel with that name already exists"); //TODO
        var channel = new Channel();
        var user = userService.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
        channel.setName(name);
        channel.setCreator(user);
        channel.setOwner(user);
        channel.setSubscribers(new ArrayList<>());
        return subscribe(user, channel);
    }

    public Channel subscribe(User user, Channel channel) {
        var users = channel.getSubscribers();
        users.add(user);
        channel.setSubscribers(users);
        return save(channel);
    }

    public Channel unsubscribe(User user, Channel channel) {
        var users = channel.getSubscribers();
        users.remove(user);
        channel.setSubscribers(users);
        return save(channel);
    }

    public Collection<User> getSubscribers(Long channelId) throws Exception {
        if (channelRepository.findById(channelId).isPresent()) {
            return channelRepository.findById(channelId).get().getSubscribers();
        }
        throw new Exception("Channel not found");
    }
}
