package org.aibles.spaced_repetition.auth.repository;

import org.aibles.spaced_repetition.auth.entity.Account;
import org.aibles.spaced_repetition.shared.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends BaseRepository<Account> {
    
    Optional<Account> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT a FROM Account a WHERE a.email = :email AND a.isActive = true")
    Optional<Account> findByEmailAndActive(@Param("email") String email);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.isActive = true")
    long countActiveAccounts();
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.isActive = false")
    long countInactiveAccounts();
}