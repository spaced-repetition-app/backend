package org.aibles.spaced_repetition.shared.service;

import lombok.RequiredArgsConstructor;
import org.aibles.spaced_repetition.shared.entity.BaseEntity;
import org.aibles.spaced_repetition.shared.exception.ResourceNotFoundException;
import org.aibles.spaced_repetition.shared.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {
    
    protected final BaseRepository<T> repository;
    
    protected abstract String getEntityName();
    
    @Override
    public T create(T entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        entity.setUpdatedAt(LocalDateTime.now());
        return repository.save(entity);
    }
    
    @Override
    public T update(String id, T entity) {
        T existingEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName(), id));
        
        entity.setId(id);
        entity.setCreatedAt(existingEntity.getCreatedAt());
        entity.setCreatedBy(existingEntity.getCreatedBy());
        entity.setUpdatedAt(LocalDateTime.now());
        
        return repository.save(entity);
    }
    
    @Override
    public Optional<T> findById(String id) {
        return repository.findById(id);
    }
    
    @Override
    public List<T> findAll() {
        return repository.findAll();
    }
    
    @Override
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    @Override
    public void deleteById(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(getEntityName(), id);
        }
        repository.deleteById(id);
    }
    
    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }
    
    @Override
    public long count() {
        return repository.count();
    }
    
    @Override
    public List<T> findByCreatedBy(String createdBy) {
        return repository.findByCreatedBy(createdBy);
    }
    
    @Override
    public Page<T> findByCreatedBy(String createdBy, Pageable pageable) {
        return repository.findByCreatedBy(createdBy, pageable);
    }
    
    @Override
    public long countByCreatedBy(String createdBy) {
        return repository.countByCreatedBy(createdBy);
    }
    
    @Override
    public Optional<T> findByIdAndCreatedBy(String id, String createdBy) {
        return repository.findByIdAndCreatedBy(id, createdBy);
    }
    
    @Override
    public boolean existsByIdAndCreatedBy(String id, String createdBy) {
        return repository.existsByIdAndCreatedBy(id, createdBy);
    }
}