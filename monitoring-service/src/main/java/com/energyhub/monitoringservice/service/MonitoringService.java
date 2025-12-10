package com.energyhub.monitoringservice.service;

import com.energyhub.monitoringservice.model.Device;
import com.energyhub.monitoringservice.model.DeviceReading;
import com.energyhub.monitoringservice.model.HourlyConsumption;
import com.energyhub.monitoringservice.repository.DeviceRepository;
import com.energyhub.monitoringservice.repository.HourlyConsumptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final HourlyConsumptionRepository hourlyConsumptionRepository;
    private final DeviceRepository deviceRepository;

    /**
     * Process a device reading and aggregate it into hourly consumption
     */
    @Transactional
    public void processDeviceReading(DeviceReading reading) {
        // Get the hour this reading belongs to (e.g., 10:00:00 for any time between 10:00 and 10:59)
        LocalDateTime hourStart = reading.getTimestamp().withMinute(0).withSecond(0).withNano(0);

        // Find or create hourly consumption record
        HourlyConsumption hourlyConsumption = hourlyConsumptionRepository
                .findByDeviceIdAndTimestamp(reading.getDeviceId(), hourStart)
                .orElse(new HourlyConsumption(
                        null,
                        reading.getDeviceId(),
                        hourStart,
                        0.0,
                        0
                ));

        // Add this reading to the hourly total
        hourlyConsumption.setTotalConsumption(
                hourlyConsumption.getTotalConsumption() + reading.getMeasurementValue()
        );
        hourlyConsumption.setReadingCount(hourlyConsumption.getReadingCount() + 1);

        // Save
        hourlyConsumptionRepository.save(hourlyConsumption);

        System.out.println("📊 Processed reading for device " + reading.getDeviceId() +
                " at " + reading.getTimestamp() +
                " | Hour total: " + hourlyConsumption.getTotalConsumption() + " kWh" +
                " | Readings: " + hourlyConsumption.getReadingCount() + "/6");
    }

    /**
     * Get hourly consumption for a device on a specific date
     */
    public List<HourlyConsumption> getDailyConsumption(Long deviceId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return hourlyConsumptionRepository.findByDeviceIdAndTimestampBetween(
                deviceId,
                startOfDay,
                endOfDay
        );
    }

    /**
     * Get all devices for a specific user
     */
    public List<Device> getDevicesByUserId(Long userId) {
        return deviceRepository.findByUserId(userId);
    }

    /**
     * Sync device from device-service
     */
    @Transactional
    public void syncDevice(Device device) {
        deviceRepository.save(device);
        System.out.println("🔄 Device synced: " + device);
    }
}