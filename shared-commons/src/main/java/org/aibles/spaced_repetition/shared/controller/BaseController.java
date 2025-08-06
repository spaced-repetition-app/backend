package org.aibles.spaced_repetition.shared.controller;

import lombok.RequiredArgsConstructor;
import org.aibles.spaced_repetition.shared.dto.BaseResponse;
import org.aibles.spaced_repetition.shared.dto.PageResponse;
import org.aibles.spaced_repetition.shared.entity.BaseEntity;
import org.aibles.spaced_repetition.shared.exception.ResourceNotFoundException;
import org.aibles.spaced_repetition.shared.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
public abstract class BaseController<T extends BaseEntity> {
    
    protected final BaseService<T> service;
    
    protected abstract String getEntityName();
    
    @PostMapping
    public ResponseEntity<BaseResponse<T>> create(@Valid @RequestBody T entity) {
        T created = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success("Created " + getEntityName() + " successfully", created));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<T>> findById(@PathVariable String id) {
        T entity = service.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName(), id));
        return ResponseEntity.ok(BaseResponse.success(entity));
    }
    
    @GetMapping
    public ResponseEntity<BaseResponse<List<T>>> findAll() {
        List<T> entities = service.findAll();
        return ResponseEntity.ok(BaseResponse.success(entities));
    }
    
    @GetMapping("/page")
    public ResponseEntity<BaseResponse<PageResponse<T>>> findAll(Pageable pageable) {
        Page<T> page = service.findAll(pageable);
        PageResponse<T> pageResponse = PageResponse.of(page);
        return ResponseEntity.ok(BaseResponse.success(pageResponse));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<T>> update(@PathVariable String id, 
                                                @Valid @RequestBody T entity) {
        T updated = service.update(id, entity);
        return ResponseEntity.ok(BaseResponse.success("Updated " + getEntityName() + " successfully", updated));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.ok(BaseResponse.success("Deleted " + getEntityName() + " successfully", null));
    }
    
    @GetMapping("/count")
    public ResponseEntity<BaseResponse<Long>> count() {
        long count = service.count();
        return ResponseEntity.ok(BaseResponse.success(count));
    }
    
    @GetMapping("/exists/{id}")
    public ResponseEntity<BaseResponse<Boolean>> existsById(@PathVariable String id) {
        boolean exists = service.existsById(id);
        return ResponseEntity.ok(BaseResponse.success(exists));
    }
    
    @GetMapping("/by-creator/{createdBy}")
    public ResponseEntity<BaseResponse<List<T>>> findByCreatedBy(@PathVariable String createdBy) {
        List<T> entities = service.findByCreatedBy(createdBy);
        return ResponseEntity.ok(BaseResponse.success(entities));
    }
    
    @GetMapping("/by-creator/{createdBy}/page")
    public ResponseEntity<BaseResponse<PageResponse<T>>> findByCreatedBy(@PathVariable String createdBy,
                                                                        Pageable pageable) {
        Page<T> page = service.findByCreatedBy(createdBy, pageable);
        PageResponse<T> pageResponse = PageResponse.of(page);
        return ResponseEntity.ok(BaseResponse.success(pageResponse));
    }
    
    @GetMapping("/by-creator/{createdBy}/count")
    public ResponseEntity<BaseResponse<Long>> countByCreatedBy(@PathVariable String createdBy) {
        long count = service.countByCreatedBy(createdBy);
        return ResponseEntity.ok(BaseResponse.success(count));
    }
}