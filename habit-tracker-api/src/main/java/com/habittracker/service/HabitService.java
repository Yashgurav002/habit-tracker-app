package com.habittracker.service;

import com.habittracker.dto.HabitRequest;
import com.habittracker.dto.HabitResponse;
import com.habittracker.entity.Habit;
import com.habittracker.entity.User;
import com.habittracker.exception.BadRequestException;
import com.habittracker.exception.ResourceNotFoundException;
import com.habittracker.repository.HabitRepository;
import com.habittracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabitService {
    
    @Autowired
    private HabitRepository habitRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public HabitResponse createHabit(HabitRequest habitRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        // Check if habit name is unique for this user
        if (habitRepository.existsByNameAndUser(habitRequest.getName(), user)) {
            throw new BadRequestException("Habit name must be unique. You already have a habit with this name.");
        }
        
        // Validate frequency type
        if (habitRequest.getFrequencyType() == null) {
            throw new BadRequestException("Frequency type is required and must be DAILY, WEEKLY, or MONTHLY");
        }
        
        Habit habit = new Habit();
        habit.setName(habitRequest.getName());
        habit.setDescription(habitRequest.getDescription());
        habit.setFrequencyType(habitRequest.getFrequencyType());
        habit.setTargetCount(habitRequest.getTargetCount() != null && habitRequest.getTargetCount() > 0 ? habitRequest.getTargetCount() : 1);
        habit.setUser(user);
        
        Habit savedHabit = habitRepository.save(habit);
        return mapToResponse(savedHabit);
    }
    
    public List<HabitResponse> getUserHabits(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        List<Habit> habits = habitRepository.findActiveHabitsByUser(user);
        return habits.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public HabitResponse getHabitById(Long habitId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Habit habit = habitRepository.findByIdAndUser(habitId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Habit", "id", habitId));
        
        return mapToResponse(habit);
    }
    
    @Transactional
    public HabitResponse updateHabit(Long habitId, HabitRequest habitRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Habit habit = habitRepository.findByIdAndUser(habitId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Habit", "id", habitId));
        
        // Check if habit name is unique for this user (if name is being changed)
        if (!habit.getName().equals(habitRequest.getName()) && 
            habitRepository.existsByNameAndUser(habitRequest.getName(), user)) {
            throw new BadRequestException("Habit name must be unique. You already have a habit with this name.");
        }
        
        // Validate frequency type
        if (habitRequest.getFrequencyType() == null) {
            throw new BadRequestException("Frequency type is required and must be DAILY, WEEKLY, or MONTHLY");
        }
        
        habit.setName(habitRequest.getName());
        habit.setDescription(habitRequest.getDescription());
        habit.setFrequencyType(habitRequest.getFrequencyType());
        habit.setTargetCount(habitRequest.getTargetCount() != null && habitRequest.getTargetCount() > 0 ? habitRequest.getTargetCount() : 1);
        
        Habit updatedHabit = habitRepository.save(habit);
        return mapToResponse(updatedHabit);
    }
    
    @Transactional
    public void deleteHabit(Long habitId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Habit habit = habitRepository.findByIdAndUser(habitId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Habit", "id", habitId));
        
        habit.setIsActive(false);
        habitRepository.save(habit);
    }
    
    public List<HabitResponse> searchHabits(String name, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        List<Habit> habits = habitRepository.findActiveHabitsByUserAndNameContaining(user, name);
        return habits.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private HabitResponse mapToResponse(Habit habit) {
        HabitResponse response = new HabitResponse();
        response.setId(habit.getId());
        response.setName(habit.getName());
        response.setDescription(habit.getDescription());
        response.setFrequencyType(habit.getFrequencyType());
        response.setTargetCount(habit.getTargetCount());
        response.setIsActive(habit.getIsActive());
        response.setStreakCount(habit.getStreakCount());
        response.setBestStreak(habit.getBestStreak());
        response.setCreatedAt(habit.getCreatedAt());
        response.setUpdatedAt(habit.getUpdatedAt());
        return response;
    }
}
