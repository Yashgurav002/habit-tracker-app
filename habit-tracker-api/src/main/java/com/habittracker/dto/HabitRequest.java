package com.habittracker.dto;

import com.habittracker.entity.FrequencyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class HabitRequest {
    
    @NotBlank(message = "Habit name is required")
    @Size(min = 2, max = 100, message = "Habit name must be between 2 and 100 characters")
    private String name;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    private FrequencyType frequencyType = FrequencyType.DAILY;
    
    @Positive(message = "Target count must be positive")
    private Integer targetCount = 1;
    
    // Constructors
    public HabitRequest() {}
    
    public HabitRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    // Getters and Setters
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
}
