package com.habittracker.repository;

import com.habittracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.id = :id")
    Optional<User> findActiveUserById(@Param("id") Long id);
    
    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.username = :username")
    Optional<User> findActiveUserByUsername(@Param("username") String username);
}
