package com.habittracker.service;

import com.habittracker.dto.HabitEntryRequest;
import com.habittracker.dto.HabitEntryResponse;
import com.habittracker.entity.Habit;
import com.habittracker.entity.HabitEntry;
import com.habittracker.entity.User;
import com.habittracker.exception.BadRequestException;
import com.habittracker.exception.ResourceNotFoundException;
import com.habittracker.repository.HabitEntryRepository;
import com.habittracker.repository.HabitRepository;
import com.habittracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HabitEntryService {
    
    @Autowired
    private HabitEntryRepository habitEntryRepository;
    
    @Autowired
    private HabitRepository habitRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public HabitEntryResponse createHabitEntry(Long habitId, HabitEntryRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Habit habit = habitRepository.findByIdAndUser(habitId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Habit", "id", habitId));
        
        // Check if entry already exists for this date and handle targetCount
        Optional<HabitEntry> existingEntry = habitEntryRepository.findByHabitAndEntryDate(habit, request.getEntryDate());
        if (existingEntry.isPresent()) {
            HabitEntry existing = existingEntry.get();
            int currentCompletionCount = existing.getCompletionCount() != null ? existing.getCompletionCount() : 0;
            int targetCount = habit.getTargetCount() != null ? habit.getTargetCount() : 1;
            
            if (currentCompletionCount >= targetCount) {
                throw new BadRequestException("Target count for this date has already been reached. Use update instead.");
            } else {
                // Update existing entry instead of creating new one
                int newCompletionCount = currentCompletionCount + (request.getCompletionCount() != null ? request.getCompletionCount() : 1);
                existing.setCompletionCount(Math.min(newCompletionCount, targetCount));
                existing.setIsCompleted(existing.getCompletionCount() >= targetCount);
                if (request.getNotes() != null && !request.getNotes().isEmpty()) {
                    existing.setNotes(request.getNotes());
                }
                
                HabitEntry updatedEntry = habitEntryRepository.save(existing);
                updateHabitStreak(habit);
                return mapToResponse(updatedEntry);
            }
        }
        
        HabitEntry habitEntry = new HabitEntry();
        habitEntry.setHabit(habit);
        habitEntry.setEntryDate(request.getEntryDate());
        habitEntry.setCompletionCount(request.getCompletionCount());
        habitEntry.setIsCompleted(request.getIsCompleted());
        habitEntry.setNotes(request.getNotes());
        
        HabitEntry savedEntry = habitEntryRepository.save(habitEntry);
        
        // Update habit streak
        updateHabitStreak(habit);
        
        return mapToResponse(savedEntry);
    }
    
    public List<HabitEntryResponse> getHabitEntries(Long habitId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Habit habit = habitRepository.findByIdAndUser(habitId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Habit", "id", habitId));
        
        List<HabitEntry> entries = habitEntryRepository.findByHabitOrderByEntryDateDesc(habit);
        return entries.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public List<HabitEntryResponse> getHabitEntriesByDateRange(Long habitId, LocalDate startDate, LocalDate endDate, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Habit habit = habitRepository.findByIdAndUser(habitId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Habit", "id", habitId));
        
        List<HabitEntry> entries = habitEntryRepository.findEntriesByHabitAndDateRange(habit, startDate, endDate);
        return entries.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public HabitEntryResponse updateHabitEntry(Long habitId, Long entryId, HabitEntryRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Habit habit = habitRepository.findByIdAndUser(habitId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Habit", "id", habitId));
        
        HabitEntry habitEntry = habitEntryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("HabitEntry", "id", entryId));
        
        if (!habitEntry.getHabit().getId().equals(habitId)) {
            throw new BadRequestException("Habit entry does not belong to this habit");
        }
        
        habitEntry.setEntryDate(request.getEntryDate());
        habitEntry.setCompletionCount(request.getCompletionCount());
        habitEntry.setIsCompleted(request.getIsCompleted());
        habitEntry.setNotes(request.getNotes());
        
        HabitEntry updatedEntry = habitEntryRepository.save(habitEntry);
        
        // Update habit streak
        updateHabitStreak(habit);
        
        return mapToResponse(updatedEntry);
    }
    
    @Transactional
    public void deleteHabitEntry(Long habitId, Long entryId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Habit habit = habitRepository.findByIdAndUser(habitId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Habit", "id", habitId));
        
        HabitEntry habitEntry = habitEntryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("HabitEntry", "id", entryId));
        
        if (!habitEntry.getHabit().getId().equals(habitId)) {
            throw new BadRequestException("Habit entry does not belong to this habit");
        }
        
        habitEntryRepository.delete(habitEntry);
        
        // Update habit streak
        updateHabitStreak(habit);
    }
    
    private void updateHabitStreak(Habit habit) {
        // Calculate current streak
        int currentStreak = calculateCurrentStreak(habit);
        habit.setStreakCount(currentStreak);
        
        // Update best streak if current is higher
        if (currentStreak > habit.getBestStreak()) {
            habit.setBestStreak(currentStreak);
        }
        
        habitRepository.save(habit);
    }
    
    private int calculateCurrentStreak(Habit habit) {
        List<HabitEntry> entries = habitEntryRepository.findByHabitOrderByEntryDateDesc(habit);
        
        if (entries.isEmpty()) {
            return 0;
        }
        
        int streak = 0;
        LocalDate currentDate = LocalDate.now();
        
        // Allow streak to start from yesterday if today hasn't been completed yet
        boolean foundRecentEntry = false;
        LocalDate checkDate = currentDate;
        
        for (HabitEntry entry : entries) {
            if (entry.getEntryDate().equals(checkDate) && entry.getIsCompleted()) {
                streak++;
                foundRecentEntry = true;
                checkDate = checkDate.minusDays(1);
            } else if (entry.getEntryDate().equals(checkDate)) {
                // Found entry for this date but not completed - streak breaks
                if (foundRecentEntry || checkDate.equals(currentDate)) {
                    break;
                }
                checkDate = checkDate.minusDays(1);
            } else if (entry.getEntryDate().isBefore(checkDate)) {
                // Missing entry for expected date - check if it's a gap
                while (checkDate.isAfter(entry.getEntryDate())) {
                    checkDate = checkDate.minusDays(1);
                }
                
                if (entry.getEntryDate().equals(checkDate) && entry.getIsCompleted()) {
                    if (foundRecentEntry) {
                        // There's a gap, streak is broken
                        break;
                    }
                    streak++;
                    foundRecentEntry = true;
                    checkDate = checkDate.minusDays(1);
                } else {
                    // Entry not completed or doesn't match expected date
                    break;
                }
            }
        }
        
        return streak;
    }
    
    private HabitEntryResponse mapToResponse(HabitEntry habitEntry) {
        HabitEntryResponse response = new HabitEntryResponse();
        response.setId(habitEntry.getId());
        response.setEntryDate(habitEntry.getEntryDate());
        response.setCompletionCount(habitEntry.getCompletionCount());
        response.setIsCompleted(habitEntry.getIsCompleted());
        response.setNotes(habitEntry.getNotes());
        response.setCreatedAt(habitEntry.getCreatedAt());
        response.setUpdatedAt(habitEntry.getUpdatedAt());
        return response;
    }
}
