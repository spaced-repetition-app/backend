package org.aibles.spaced_repetition.shared.repository;

import org.aibles.spaced_repetition.shared.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, String> {

    @Query("SELECT e FROM #{#entityName} e WHERE e.createdBy = :createdBy")
    List<T> findByCreatedBy(@Param("createdBy") String createdBy);

    @Query("SELECT e FROM #{#entityName} e WHERE e.createdBy = :createdBy")
    Page<T> findByCreatedBy(@Param("createdBy") String createdBy, Pageable pageable);

    @Query("SELECT e FROM #{#entityName} e WHERE e.createdAt BETWEEN :startDate AND :endDate")
    List<T> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e FROM #{#entityName} e WHERE e.createdAt >= :date")
    List<T> findByCreatedAtAfter(@Param("date") LocalDateTime date);

    @Query("SELECT e FROM #{#entityName} e WHERE e.updatedAt >= :date")
    List<T> findByUpdatedAtAfter(@Param("date") LocalDateTime date);

    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.createdBy = :createdBy")
    long countByCreatedBy(@Param("createdBy") String createdBy);

    Optional<T> findByIdAndCreatedBy(String id, String createdBy);

    boolean existsByIdAndCreatedBy(String id, String createdBy);
}