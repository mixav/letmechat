package com.damnteam.letmechat.data.dao;

import com.damnteam.letmechat.data.model.Channel;
import org.springframework.data.repository.CrudRepository;

public interface ChannelRepository extends CrudRepository<Channel, Long> {
    Channel findByName(String name);
}
