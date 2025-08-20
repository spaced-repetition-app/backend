package org.aibles.spaced_repetition.shared.service;

import org.aibles.spaced_repetition.shared.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BaseService<T extends BaseEntity> {
    
    T create(T entity);
    
    T update(String id, T entity);
    
    Optional<T> findById(String id);
    
    List<T> findAll();
    
    Page<T> findAll(Pageable pageable);
    
    void deleteById(String id);
    
    boolean existsById(String id);
    
    long count();
    
    List<T> findByCreatedBy(String createdBy);
    
    Page<T> findByCreatedBy(String createdBy, Pageable pageable);
    
    long countByCreatedBy(String createdBy);
    
    Optional<T> findByIdAndCreatedBy(String id, String createdBy);
    
    boolean existsByIdAndCreatedBy(String id, String createdBy);
}