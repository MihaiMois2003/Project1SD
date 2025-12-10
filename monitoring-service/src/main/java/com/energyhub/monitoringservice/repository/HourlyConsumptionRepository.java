package com.energyhub.monitoringservice.repository;

import com.energyhub.monitoringservice.model.HourlyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HourlyConsumptionRepository extends JpaRepository<HourlyConsumption, Long> {

    // Find hourly consumption for a specific device and time range
    List<HourlyConsumption> findByDeviceIdAndTimestampBetween(
            Long deviceId,
            LocalDateTime start,
            LocalDateTime end
    );

    // Find if we already have a record for this device and hour
    Optional<HourlyConsumption> findByDeviceIdAndTimestamp(Long deviceId, LocalDateTime timestamp);
}