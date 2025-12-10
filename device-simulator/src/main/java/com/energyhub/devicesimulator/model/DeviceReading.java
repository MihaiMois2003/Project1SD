package com.energyhub.devicesimulator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceReading implements Serializable {
    private LocalDateTime timestamp;
    private Long deviceId;
    private Double measurementValue; // Energy consumed in kWh for 10 minutes
}