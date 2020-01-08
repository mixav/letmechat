package com.damnteam.letmechat.data.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface GenericRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
    Optional<T> findByName(String name);

    List<T> findAll();

}
