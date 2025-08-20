package com.habittracker.controller;

import com.habittracker.dto.ApiResponse;
import com.habittracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/db-test")
    public ResponseEntity<ApiResponse<Map<String, Object>>> testDatabase() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            long userCount = userRepository.count();
            result.put("database_connected", true);
            result.put("user_count", userCount);
            result.put("status", "SUCCESS");
        } catch (Exception e) {
            result.put("database_connected", false);
            result.put("error", e.getMessage());
            result.put("status", "FAILED");
        }
        
        return ResponseEntity.ok(ApiResponse.success("Database test completed", result));
    }
}
