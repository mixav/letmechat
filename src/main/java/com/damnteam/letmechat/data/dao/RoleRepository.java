package com.damnteam.letmechat.data.dao;

import com.damnteam.letmechat.data.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
