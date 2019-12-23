package com.damnteam.letmechat.repositories;

import com.damnteam.letmechat.models.User;
import com.damnteam.letmechat.models.UserData;
import org.springframework.data.repository.CrudRepository;

public interface UserDataRepository extends CrudRepository<UserData, Long> {
    UserData findByUser(User user);
}