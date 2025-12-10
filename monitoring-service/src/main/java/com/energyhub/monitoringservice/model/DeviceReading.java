package com.energyhub.monitoringservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for device readings coming from the simulator
 * This is NOT stored in the database directly
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceReading implements Serializable {
    private LocalDateTime timestamp;
    private Long deviceId;
    private Double measurementValue; // Energy consumed in the last 10 minutes (kWh)
}