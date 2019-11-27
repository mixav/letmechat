package com.damnteam.letmechat.data.dao;

import com.damnteam.letmechat.data.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByLogin(String login);
}
