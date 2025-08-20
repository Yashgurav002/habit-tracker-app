package com.habittracker.repository;

import com.habittracker.entity.Habit;
import com.habittracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
    
    List<Habit> findByUserAndIsActiveTrue(User user);
    
    List<Habit> findByUser(User user);
    
    Optional<Habit> findByIdAndUser(Long id, User user);
    
    @Query("SELECT h FROM Habit h WHERE h.user = :user AND h.isActive = true ORDER BY h.createdAt DESC")
    List<Habit> findActiveHabitsByUser(@Param("user") User user);
    
    @Query("SELECT h FROM Habit h WHERE h.user.id = :userId AND h.isActive = true")
    List<Habit> findActiveHabitsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(h) FROM Habit h WHERE h.user = :user AND h.isActive = true")
    Long countActiveHabitsByUser(@Param("user") User user);
    
    @Query("SELECT h FROM Habit h WHERE h.user = :user AND h.name LIKE %:name% AND h.isActive = true")
    List<Habit> findActiveHabitsByUserAndNameContaining(@Param("user") User user, @Param("name") String name);
    
    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END FROM Habit h WHERE h.name = :name AND h.user = :user AND h.isActive = true")
    boolean existsByNameAndUser(@Param("name") String name, @Param("user") User user);
}
