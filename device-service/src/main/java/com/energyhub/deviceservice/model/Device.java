package com.energyhub.deviceservice.model;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private String address;

    @Column(nullable = false)
    private Double maxConsumption; // in kWh

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    private Long userId; // Which user owns this device

    public enum Status {
        ACTIVE, INACTIVE
    }
}