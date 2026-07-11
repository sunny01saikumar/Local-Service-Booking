package com.sai.geoLocation.controller;

import com.sai.geoLocation.common.ApiResponse;
import com.sai.geoLocation.entity.BaseEntity;
import com.sai.geoLocation.service.CrudService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

public abstract class CrudController<T extends BaseEntity> {
    private final CrudService<T> service;

    protected CrudController(CrudService<T> service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<Page<T>> findAll(Pageable pageable) {
        return ApiResponse.ok("Fetched successfully", service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<T> findById(@PathVariable UUID id) {
        return ApiResponse.ok("Fetched successfully", service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<T>> create(@Valid @RequestBody T request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Created successfully", service.save(request)));
    }

    @PutMapping("/{id}")
    public ApiResponse<T> update(@PathVariable UUID id, @Valid @RequestBody T request) {
        request.setId(id);
        return ApiResponse.ok("Updated successfully", service.save(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
