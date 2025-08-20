package com.habittracker.controller;

import com.habittracker.dto.AnalyticsResponse;
import com.habittracker.dto.ApiResponse;
import com.habittracker.security.UserPrincipal;
import com.habittracker.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    
    @Autowired
    private AnalyticsService analyticsService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<AnalyticsResponse>> getUserAnalytics(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        AnalyticsResponse analytics = analyticsService.getUserAnalytics(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Analytics retrieved successfully", analytics));
    }
    
    @GetMapping("/habits/{habitId}")
    public ResponseEntity<ApiResponse<AnalyticsResponse>> getHabitAnalytics(
            @PathVariable Long habitId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        AnalyticsResponse analytics = analyticsService.getHabitAnalytics(habitId, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Habit analytics retrieved successfully", analytics));
    }
}
