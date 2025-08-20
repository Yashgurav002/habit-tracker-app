package com.habittracker.controller;

import com.habittracker.dto.ApiResponse;
import com.habittracker.dto.HabitRequest;
import com.habittracker.dto.HabitResponse;
import com.habittracker.security.UserPrincipal;
import com.habittracker.service.HabitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
public class HabitController {
    
    @Autowired
    private HabitService habitService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<HabitResponse>> createHabit(
            @Valid @RequestBody HabitRequest habitRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        HabitResponse habit = habitService.createHabit(habitRequest, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Habit created successfully", habit));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<HabitResponse>>> getUserHabits(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<HabitResponse> habits = habitService.getUserHabits(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Habits retrieved successfully", habits));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HabitResponse>> getHabitById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        HabitResponse habit = habitService.getHabitById(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Habit retrieved successfully", habit));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HabitResponse>> updateHabit(
            @PathVariable Long id,
            @Valid @RequestBody HabitRequest habitRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        HabitResponse habit = habitService.updateHabit(id, habitRequest, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Habit updated successfully", habit));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteHabit(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        habitService.deleteHabit(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Habit deleted successfully"));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<HabitResponse>>> searchHabits(
            @RequestParam String name,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<HabitResponse> habits = habitService.searchHabits(name, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Habits found", habits));
    }
}
