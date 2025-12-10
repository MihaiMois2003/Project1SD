package com.energyhub.monitoringservice.controller;

import com.energyhub.monitoringservice.model.HourlyConsumption;
import com.energyhub.monitoringservice.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MonitoringController {

    private final MonitoringService monitoringService;

    /**
     * Get daily consumption for a specific device
     * Example: GET /api/monitoring/device/1/daily?date=2025-12-09
     */
    @GetMapping("/device/{deviceId}/daily")
    public ResponseEntity<List<HourlyConsumption>> getDailyConsumption(
            @PathVariable Long deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<HourlyConsumption> consumption = monitoringService.getDailyConsumption(deviceId, date);
        return ResponseEntity.ok(consumption);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "monitoring-service");
        health.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(health);
    }
}