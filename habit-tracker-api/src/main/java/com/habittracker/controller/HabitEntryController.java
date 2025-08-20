package com.habittracker.controller;

import com.habittracker.dto.ApiResponse;
import com.habittracker.dto.HabitEntryRequest;
import com.habittracker.dto.HabitEntryResponse;
import com.habittracker.security.UserPrincipal;
import com.habittracker.service.HabitEntryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/habits/{habitId}/entries")
public class HabitEntryController {
    
    @Autowired
    private HabitEntryService habitEntryService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<HabitEntryResponse>> createHabitEntry(
            @PathVariable Long habitId,
            @Valid @RequestBody HabitEntryRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        HabitEntryResponse habitEntry = habitEntryService.createHabitEntry(habitId, request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Habit entry created successfully", habitEntry));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<HabitEntryResponse>>> getHabitEntries(
            @PathVariable Long habitId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<HabitEntryResponse> entries;
        
        if (startDate != null && endDate != null) {
            entries = habitEntryService.getHabitEntriesByDateRange(habitId, startDate, endDate, userPrincipal.getId());
        } else {
            entries = habitEntryService.getHabitEntries(habitId, userPrincipal.getId());
        }
        
        return ResponseEntity.ok(ApiResponse.success("Habit entries retrieved successfully", entries));
    }
    
    @PutMapping("/{entryId}")
    public ResponseEntity<ApiResponse<HabitEntryResponse>> updateHabitEntry(
            @PathVariable Long habitId,
            @PathVariable Long entryId,
            @Valid @RequestBody HabitEntryRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        HabitEntryResponse habitEntry = habitEntryService.updateHabitEntry(habitId, entryId, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Habit entry updated successfully", habitEntry));
    }
    
    @DeleteMapping("/{entryId}")
    public ResponseEntity<ApiResponse<String>> deleteHabitEntry(
            @PathVariable Long habitId,
            @PathVariable Long entryId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        habitEntryService.deleteHabitEntry(habitId, entryId, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Habit entry deleted successfully"));
    }
}
