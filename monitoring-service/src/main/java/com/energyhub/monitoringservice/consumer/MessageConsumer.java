package com.energyhub.monitoringservice.consumer;

import com.energyhub.monitoringservice.dto.DeviceSyncDTO;
import com.energyhub.monitoringservice.model.Device;
import com.energyhub.monitoringservice.model.DeviceReading;
import com.energyhub.monitoringservice.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private final MonitoringService monitoringService;

    /**
     * Listen to device synchronization messages from device-service
     */
    @RabbitListener(queues = "device.sync")
    public void consumeDeviceSync(DeviceSyncDTO deviceDTO) {
        System.out.println("📥 Received device sync: " + deviceDTO);

        // Convert DTO to Device entity
        Device device = new Device(
                deviceDTO.getId(),
                deviceDTO.getName(),
                deviceDTO.getMaxConsumption(),
                deviceDTO.getUserId()
        );

        // Save to monitoring database
        monitoringService.syncDevice(device);
    }

    /**
     * Listen to device readings from simulator
     */
    @RabbitListener(queues = "device.readings")
    public void consumeDeviceReading(DeviceReading reading) {
        System.out.println("📥 Received device reading: Device " + reading.getDeviceId() +
                " at " + reading.getTimestamp() +
                " → " + reading.getMeasurementValue() + " kWh");

        // Process and aggregate the reading
        monitoringService.processDeviceReading(reading);
    }
}