package com.damnteam.letmechat.data.dao;

import com.damnteam.letmechat.data.model.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
}
