package com.habittracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class HabitEntryRequest {
    
    @NotNull(message = "Entry date is required")
    private LocalDate entryDate;
    
    @Positive(message = "Completion count must be positive")
    private Integer completionCount = 1;
    
    private Boolean isCompleted = true;
    
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
    
    // Constructors
    public HabitEntryRequest() {}
    
    public HabitEntryRequest(LocalDate entryDate) {
        this.entryDate = entryDate;
    }
    
    public HabitEntryRequest(LocalDate entryDate, Integer completionCount) {
        this.entryDate = entryDate;
        this.completionCount = completionCount;
    }
    
    // Getters and Setters
    public LocalDate getEntryDate() {
        return entryDate;
    }
    
    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }
    
    public Integer getCompletionCount() {
        return completionCount;
    }
    
    public void setCompletionCount(Integer completionCount) {
        this.completionCount = completionCount;
    }
    
    public Boolean getIsCompleted() {
        return isCompleted;
    }
    
    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
