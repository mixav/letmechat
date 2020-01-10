package com.damnteam.letmechat.data.dao;

import com.damnteam.letmechat.data.model.Channel;
import com.damnteam.letmechat.data.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findTop30ByChannelOrderByTimeDesc(Channel channelId);

    List<Message> findByChannel(Channel channel, Pageable pageable);
}
