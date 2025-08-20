package com.habittracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "habits")
@EntityListeners(AuditingEntityListener.class)
public class Habit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Habit name is required")
    @Size(min = 2, max = 100, message = "Habit name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "frequency_type")
    private FrequencyType frequencyType = FrequencyType.DAILY;
    
    @Column(name = "target_count")
    private Integer targetCount = 1;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "streak_count")
    private Integer streakCount = 0;
    
    @Column(name = "best_streak")
    private Integer bestStreak = 0;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<HabitEntry> habitEntries = new HashSet<>();
    
    // Constructors
    public Habit() {}
    
    public Habit(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
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
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Set<HabitEntry> getHabitEntries() {
        return habitEntries;
    }
    
    public void setHabitEntries(Set<HabitEntry> habitEntries) {
        this.habitEntries = habitEntries;
    }
}
