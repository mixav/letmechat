package com.damnteam.letmechat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.EntityNotFoundException;

public class GenericService<T> {

    @Autowired
    private CrudRepository<T, Long> repository;

    public T findById(Long id) throws Exception {
        var result = repository.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("Entity not found");
    }

    public T save(T entity) {
        return repository.save(entity);
    }
}
