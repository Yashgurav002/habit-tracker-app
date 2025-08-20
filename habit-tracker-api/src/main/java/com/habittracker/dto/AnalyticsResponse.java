package com.habittracker.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AnalyticsResponse {
    
    private Long totalHabits;
    private Long activeHabits;
    private Long completedEntriesThisWeek;
    private Long completedEntriesThisMonth;
    private Double weeklyCompletionRate;
    private Double monthlyCompletionRate;
    private Integer longestStreak;
    private Integer currentStreak;
    private List<HabitStreakInfo> habitStreaks;
    private Map<LocalDate, Integer> dailyCompletions;
    private Map<String, Integer> habitCompletionCounts;
    
    // Constructors
    public AnalyticsResponse() {}
    
    // Getters and Setters
    public Long getTotalHabits() {
        return totalHabits;
    }
    
    public void setTotalHabits(Long totalHabits) {
        this.totalHabits = totalHabits;
    }
    
    public Long getActiveHabits() {
        return activeHabits;
    }
    
    public void setActiveHabits(Long activeHabits) {
        this.activeHabits = activeHabits;
    }
    
    public Long getCompletedEntriesThisWeek() {
        return completedEntriesThisWeek;
    }
    
    public void setCompletedEntriesThisWeek(Long completedEntriesThisWeek) {
        this.completedEntriesThisWeek = completedEntriesThisWeek;
    }
    
    public Long getCompletedEntriesThisMonth() {
        return completedEntriesThisMonth;
    }
    
    public void setCompletedEntriesThisMonth(Long completedEntriesThisMonth) {
        this.completedEntriesThisMonth = completedEntriesThisMonth;
    }
    
    public Double getWeeklyCompletionRate() {
        return weeklyCompletionRate;
    }
    
    public void setWeeklyCompletionRate(Double weeklyCompletionRate) {
        this.weeklyCompletionRate = weeklyCompletionRate;
    }
    
    public Double getMonthlyCompletionRate() {
        return monthlyCompletionRate;
    }
    
    public void setMonthlyCompletionRate(Double monthlyCompletionRate) {
        this.monthlyCompletionRate = monthlyCompletionRate;
    }
    
    public Integer getLongestStreak() {
        return longestStreak;
    }
    
    public void setLongestStreak(Integer longestStreak) {
        this.longestStreak = longestStreak;
    }
    
    public Integer getCurrentStreak() {
        return currentStreak;
    }
    
    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }
    
    public List<HabitStreakInfo> getHabitStreaks() {
        return habitStreaks;
    }
    
    public void setHabitStreaks(List<HabitStreakInfo> habitStreaks) {
        this.habitStreaks = habitStreaks;
    }
    
    public Map<LocalDate, Integer> getDailyCompletions() {
        return dailyCompletions;
    }
    
    public void setDailyCompletions(Map<LocalDate, Integer> dailyCompletions) {
        this.dailyCompletions = dailyCompletions;
    }
    
    public Map<String, Integer> getHabitCompletionCounts() {
        return habitCompletionCounts;
    }
    
    public void setHabitCompletionCounts(Map<String, Integer> habitCompletionCounts) {
        this.habitCompletionCounts = habitCompletionCounts;
    }
    
    // Inner class for habit streak information
    public static class HabitStreakInfo {
        private Long habitId;
        private String habitName;
        private Integer currentStreak;
        private Integer bestStreak;
        
        public HabitStreakInfo() {}
        
        public HabitStreakInfo(Long habitId, String habitName, Integer currentStreak, Integer bestStreak) {
            this.habitId = habitId;
            this.habitName = habitName;
            this.currentStreak = currentStreak;
            this.bestStreak = bestStreak;
        }
        
        // Getters and Setters
        public Long getHabitId() {
            return habitId;
        }
        
        public void setHabitId(Long habitId) {
            this.habitId = habitId;
        }
        
        public String getHabitName() {
            return habitName;
        }
        
        public void setHabitName(String habitName) {
            this.habitName = habitName;
        }
        
        public Integer getCurrentStreak() {
            return currentStreak;
        }
        
        public void setCurrentStreak(Integer currentStreak) {
            this.currentStreak = currentStreak;
        }
        
        public Integer getBestStreak() {
            return bestStreak;
        }
        
        public void setBestStreak(Integer bestStreak) {
            this.bestStreak = bestStreak;
        }
    }
}
