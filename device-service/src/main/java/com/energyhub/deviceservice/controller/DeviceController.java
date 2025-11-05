package com.energyhub.deviceservice.controller;

import com.energyhub.deviceservice.model.Device;
import com.energyhub.deviceservice.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.getDeviceById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Device>> getDevicesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(deviceService.getDevicesByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceService.createDevice(device));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable Long id, @RequestBody Device device) {
        return ResponseEntity.ok(deviceService.updateDevice(id, device));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{deviceId}/assign/{userId}")
    public ResponseEntity<Device> assignDevice(@PathVariable Long deviceId, @PathVariable Long userId) {
        return ResponseEntity.ok(deviceService.assignDeviceToUser(deviceId, userId));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "device-service");
        health.put("instance", System.getenv().getOrDefault("INSTANCE_ID", "unknown"));
        health.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(health);
    }
}