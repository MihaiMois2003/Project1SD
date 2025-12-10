package com.energyhub.devicesimulator.service;

import com.energyhub.devicesimulator.config.RabbitMQConfig;
import com.energyhub.devicesimulator.model.DeviceReading;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DataGenerator {

    private final RabbitTemplate rabbitTemplate;
    private final Random random = new Random();

    @Value("${simulator.device.id}")
    private Long deviceId;

    /**
     * Generate and send a device reading every 10 minutes (600,000 milliseconds)
     */
    @Scheduled(fixedRate = 600000) // 10 minutes in milliseconds
    public void generateAndSend() {
        LocalDateTime now = LocalDateTime.now();
        Double measurementValue = generateRealisticValue(now);

        DeviceReading reading = new DeviceReading(now, deviceId, measurementValue);

        // Send to RabbitMQ
        rabbitTemplate.convertAndSend(RabbitMQConfig.DEVICE_READINGS_QUEUE, reading);

        System.out.println("📤 Sent reading: Device " + deviceId +
                " | Time: " + now +
                " | Value: " + String.format("%.2f", measurementValue) + " kWh");
    }

    /**
     * Generate realistic energy consumption values based on time of day
     * Lower consumption at night, higher in evening, moderate during day
     */
    private Double generateRealisticValue(LocalDateTime time) {
        int hour = time.getHour();
        double baseConsumption;

        if (hour >= 0 && hour < 6) {
            // Night time: 0.1 - 0.3 kWh per 10 minutes
            baseConsumption = 0.1 + (random.nextDouble() * 0.2);
        } else if (hour >= 6 && hour < 9) {
            // Morning: 0.4 - 0.8 kWh per 10 minutes
            baseConsumption = 0.4 + (random.nextDouble() * 0.4);
        } else if (hour >= 9 && hour < 18) {
            // Day time: 0.3 - 0.6 kWh per 10 minutes
            baseConsumption = 0.3 + (random.nextDouble() * 0.3);
        } else if (hour >= 18 && hour < 23) {
            // Evening (peak): 0.8 - 1.5 kWh per 10 minutes
            baseConsumption = 0.8 + (random.nextDouble() * 0.7);
        } else {
            // Late evening: 0.5 - 0.9 kWh per 10 minutes
            baseConsumption = 0.5 + (random.nextDouble() * 0.4);
        }

        // Add small random variation to make it more realistic
        double variation = (random.nextDouble() * 0.1) - 0.05; // ±0.05 kWh
        return Math.max(0.05, baseConsumption + variation); // Minimum 0.05 kWh
    }
}