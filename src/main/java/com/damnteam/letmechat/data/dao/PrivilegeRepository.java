package com.damnteam.letmechat.data.dao;

import com.damnteam.letmechat.data.model.Privilege;
import org.springframework.data.repository.CrudRepository;

public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {
    public Privilege findByName(String name);
}
