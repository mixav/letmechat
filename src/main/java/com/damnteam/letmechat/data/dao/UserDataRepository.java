package com.damnteam.letmechat.data.dao;

import com.damnteam.letmechat.data.model.UserData;
import org.springframework.data.repository.CrudRepository;

public interface UserDataRepository extends CrudRepository<UserData, Long> {
}
