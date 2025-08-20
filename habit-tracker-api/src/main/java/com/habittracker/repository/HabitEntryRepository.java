package com.habittracker.repository;

import com.habittracker.entity.Habit;
import com.habittracker.entity.HabitEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitEntryRepository extends JpaRepository<HabitEntry, Long> {
    
    Optional<HabitEntry> findByHabitAndEntryDate(Habit habit, LocalDate entryDate);
    
    List<HabitEntry> findByHabitOrderByEntryDateDesc(Habit habit);
    
    List<HabitEntry> findByHabitAndEntryDateBetween(Habit habit, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT he FROM HabitEntry he WHERE he.habit = :habit AND he.entryDate BETWEEN :startDate AND :endDate ORDER BY he.entryDate DESC")
    List<HabitEntry> findEntriesByHabitAndDateRange(@Param("habit") Habit habit, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(he) FROM HabitEntry he WHERE he.habit = :habit AND he.isCompleted = true")
    Long countCompletedEntriesByHabit(@Param("habit") Habit habit);
    
    @Query("SELECT COUNT(he) FROM HabitEntry he WHERE he.habit = :habit AND he.entryDate BETWEEN :startDate AND :endDate AND he.isCompleted = true")
    Long countCompletedEntriesByHabitAndDateRange(@Param("habit") Habit habit, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT he FROM HabitEntry he WHERE he.habit.user.id = :userId AND he.entryDate = :date")
    List<HabitEntry> findEntriesByUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
    
    @Query("SELECT he FROM HabitEntry he WHERE he.habit.user.id = :userId AND he.entryDate BETWEEN :startDate AND :endDate")
    List<HabitEntry> findEntriesByUserAndDateRange(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
