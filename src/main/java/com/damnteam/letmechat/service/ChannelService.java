package com.damnteam.letmechat.service;

import com.damnteam.letmechat.data.dao.ChannelRepository;
import com.damnteam.letmechat.data.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelService extends GenericService<Channel> {

    @Autowired
    private ChannelRepository channelRepository;
}
