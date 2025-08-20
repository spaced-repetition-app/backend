package org.aibles.spaced_repetition.auth.repository;

import org.aibles.spaced_repetition.auth.entity.User;
import org.aibles.spaced_repetition.shared.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {
    
    Optional<User> findByAccountId(String accountId);
    
    @Query("SELECT u FROM User u WHERE u.fullName LIKE %:name%")
    List<User> findByFullNameContaining(@Param("name") String name);
    
    @Query("SELECT u FROM User u WHERE u.avatarUrl IS NOT NULL")
    List<User> findUsersWithAvatar();
    
    boolean existsByAccountId(String accountId);
}