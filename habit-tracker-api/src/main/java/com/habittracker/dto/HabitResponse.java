package com.habittracker.dto;

import com.habittracker.entity.FrequencyType;

import java.time.LocalDateTime;

public class HabitResponse {
    
    private Long id;
    private String name;
    private String description;
    private FrequencyType frequencyType;
    private Integer targetCount;
    private Boolean isActive;
    private Integer streakCount;
    private Integer bestStreak;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public HabitResponse() {}
    
    public HabitResponse(Long id, String name, String description, FrequencyType frequencyType, Integer targetCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.frequencyType = frequencyType;
        this.targetCount = targetCount;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public FrequencyType getFrequencyType() {
        return frequencyType;
    }
    
    public void setFrequencyType(FrequencyType frequencyType) {
        this.frequencyType = frequencyType;
    }
    
    public Integer getTargetCount() {
        return targetCount;
    }
    
    public void setTargetCount(Integer targetCount) {
        this.targetCount = targetCount;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Integer getStreakCount() {
        return streakCount;
    }
    
    public void setStreakCount(Integer streakCount) {
        this.streakCount = streakCount;
    }
    
    public Integer getBestStreak() {
        return bestStreak;
    }
    
    public void setBestStreak(Integer bestStreak) {
        this.bestStreak = bestStreak;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
