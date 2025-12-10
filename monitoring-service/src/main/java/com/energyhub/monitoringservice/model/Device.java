package com.energyhub.monitoringservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    private Long id; // Same ID as in device-service

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double maxConsumption;

    private Long userId; // Which user owns this device
}