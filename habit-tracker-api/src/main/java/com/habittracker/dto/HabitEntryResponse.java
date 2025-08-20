package com.habittracker.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HabitEntryResponse {
    
    private Long id;
    private LocalDate entryDate;
    private Integer completionCount;
    private Boolean isCompleted;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public HabitEntryResponse() {}
    
    public HabitEntryResponse(Long id, LocalDate entryDate, Integer completionCount, Boolean isCompleted) {
        this.id = id;
        this.entryDate = entryDate;
        this.completionCount = completionCount;
        this.isCompleted = isCompleted;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
