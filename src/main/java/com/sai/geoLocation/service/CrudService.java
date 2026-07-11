package com.sai.geoLocation.service;

import com.sai.geoLocation.entity.BaseEntity;
import com.sai.geoLocation.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public abstract class CrudService<T extends BaseEntity> {
    private final JpaRepository<T, UUID> repository;

    protected CrudService(JpaRepository<T, UUID> repository) {
        this.repository = repository;
    }

    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public T findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + id));
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        T entity = findById(id);
        entity.setDeleted(true);
        entity.setActive(false);
        repository.save(entity);
    }
}
