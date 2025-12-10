package com.energyhub.monitoringservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceSyncDTO implements Serializable {
    private Long id;
    private String name;
    private Double maxConsumption;
    private Long userId;
}