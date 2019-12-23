package com.damnteam.letmechat.repositories;

import com.damnteam.letmechat.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUserName(String userName);
}
