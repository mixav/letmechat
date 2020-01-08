package com.damnteam.letmechat.data.dao;

import com.damnteam.letmechat.data.model.User;
import com.damnteam.letmechat.data.model.UserData;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDataRepository extends CrudRepository<UserData, Long> {
    Optional<UserData> findByUser(User user);
}
