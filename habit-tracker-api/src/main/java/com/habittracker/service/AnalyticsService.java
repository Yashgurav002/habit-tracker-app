package com.habittracker.service;

import com.habittracker.dto.AnalyticsResponse;
import com.habittracker.entity.Habit;
import com.habittracker.entity.HabitEntry;
import com.habittracker.entity.User;
import com.habittracker.exception.ResourceNotFoundException;
import com.habittracker.repository.HabitEntryRepository;
import com.habittracker.repository.HabitRepository;
import com.habittracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private HabitRepository habitRepository;
    
    @Autowired
    private HabitEntryRepository habitEntryRepository;
    
    public AnalyticsResponse getUserAnalytics(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        AnalyticsResponse analytics = new AnalyticsResponse();
        
        // Basic habit statistics
        List<Habit> allHabits = habitRepository.findByUser(user);
        List<Habit> activeHabits = habitRepository.findActiveHabitsByUser(user);
        
        analytics.setTotalHabits((long) allHabits.size());
        analytics.setActiveHabits((long) activeHabits.size());
        
        // Date ranges
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusWeeks(1);
        LocalDate monthStart = today.minusMonths(1);
        
        // Weekly and monthly completion statistics
        Long weeklyCompletions = habitEntryRepository.findEntriesByUserAndDateRange(userId, weekStart, today)
                .stream()
                .filter(HabitEntry::getIsCompleted)
                .count();
        
        Long monthlyCompletions = habitEntryRepository.findEntriesByUserAndDateRange(userId, monthStart, today)
                .stream()
                .filter(HabitEntry::getIsCompleted)
                .count();
        
        analytics.setCompletedEntriesThisWeek(weeklyCompletions);
        analytics.setCompletedEntriesThisMonth(monthlyCompletions);
        
        // Completion rates
        long totalPossibleWeeklyEntries = activeHabits.size() * 7L;
        long totalPossibleMonthlyEntries = activeHabits.size() * 30L;
        
        double weeklyCompletionRate = totalPossibleWeeklyEntries > 0 ? 
            (double) weeklyCompletions / totalPossibleWeeklyEntries * 100 : 0.0;
        double monthlyCompletionRate = totalPossibleMonthlyEntries > 0 ? 
            (double) monthlyCompletions / totalPossibleMonthlyEntries * 100 : 0.0;
        
        analytics.setWeeklyCompletionRate(weeklyCompletionRate);
        analytics.setMonthlyCompletionRate(monthlyCompletionRate);
        
        // Streak information
        List<AnalyticsResponse.HabitStreakInfo> habitStreaks = activeHabits.stream()
                .map(habit -> new AnalyticsResponse.HabitStreakInfo(
                    habit.getId(),
                    habit.getName(),
                    habit.getStreakCount(),
                    habit.getBestStreak()
                ))
                .collect(Collectors.toList());
        
        analytics.setHabitStreaks(habitStreaks);
        
        // Overall streaks
        Integer longestStreak = activeHabits.stream()
                .mapToInt(Habit::getBestStreak)
                .max()
                .orElse(0);
        
        Integer currentStreak = activeHabits.stream()
                .mapToInt(Habit::getStreakCount)
                .max()
                .orElse(0);
        
        analytics.setLongestStreak(longestStreak);
        analytics.setCurrentStreak(currentStreak);
        
        // Daily completions for the last 30 days
        Map<LocalDate, Integer> dailyCompletions = new HashMap<>();
        for (int i = 0; i < 30; i++) {
            LocalDate date = today.minusDays(i);
            List<HabitEntry> entriesForDate = habitEntryRepository.findEntriesByUserAndDate(userId, date);
            int completedCount = (int) entriesForDate.stream()
                    .filter(HabitEntry::getIsCompleted)
                    .count();
            dailyCompletions.put(date, completedCount);
        }
        analytics.setDailyCompletions(dailyCompletions);
        
        // Habit completion counts
        Map<String, Integer> habitCompletionCounts = new HashMap<>();
        for (Habit habit : activeHabits) {
            Long completedCount = habitEntryRepository.countCompletedEntriesByHabit(habit);
            habitCompletionCounts.put(habit.getName(), completedCount.intValue());
        }
        analytics.setHabitCompletionCounts(habitCompletionCounts);
        
        return analytics;
    }
    
    public AnalyticsResponse getHabitAnalytics(Long habitId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Habit habit = habitRepository.findByIdAndUser(habitId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Habit", "id", habitId));
        
        AnalyticsResponse analytics = new AnalyticsResponse();
        
        // Basic statistics
        analytics.setTotalHabits(1L);
        analytics.setActiveHabits(habit.getIsActive() ? 1L : 0L);
        
        // Date ranges
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusWeeks(1);
        LocalDate monthStart = today.minusMonths(1);
        
        // Weekly and monthly completion statistics for this habit
        Long weeklyCompletions = habitEntryRepository.countCompletedEntriesByHabitAndDateRange(habit, weekStart, today);
        Long monthlyCompletions = habitEntryRepository.countCompletedEntriesByHabitAndDateRange(habit, monthStart, today);
        
        analytics.setCompletedEntriesThisWeek(weeklyCompletions);
        analytics.setCompletedEntriesThisMonth(monthlyCompletions);
        
        // Completion rates
        long daysInWeek = ChronoUnit.DAYS.between(weekStart, today);
        long daysInMonth = ChronoUnit.DAYS.between(monthStart, today);
        
        double weeklyCompletionRate = daysInWeek > 0 ? (double) weeklyCompletions / daysInWeek * 100 : 0.0;
        double monthlyCompletionRate = daysInMonth > 0 ? (double) monthlyCompletions / daysInMonth * 100 : 0.0;
        
        analytics.setWeeklyCompletionRate(weeklyCompletionRate);
        analytics.setMonthlyCompletionRate(monthlyCompletionRate);
        
        // Streak information
        analytics.setCurrentStreak(habit.getStreakCount());
        analytics.setLongestStreak(habit.getBestStreak());
        
        // Daily completions for the last 30 days
        Map<LocalDate, Integer> dailyCompletions = new HashMap<>();
        for (int i = 0; i < 30; i++) {
            LocalDate date = today.minusDays(i);
            List<HabitEntry> entriesForDate = habitEntryRepository.findEntriesByHabitAndDateRange(habit, date, date);
            int completedCount = (int) entriesForDate.stream()
                    .filter(HabitEntry::getIsCompleted)
                    .mapToInt(HabitEntry::getCompletionCount)
                    .sum();
            dailyCompletions.put(date, completedCount);
        }
        analytics.setDailyCompletions(dailyCompletions);
        
        return analytics;
    }
}
