package com.energyhub.deviceservice.service;

import com.energyhub.deviceservice.model.Device;
import com.energyhub.deviceservice.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Device getDeviceById(Long id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));
    }

    public List<Device> getDevicesByUserId(Long userId) {
        return deviceRepository.findByUserId(userId);
    }

    public Device createDevice(Device device) {
        return deviceRepository.save(device);
    }

    public Device updateDevice(Long id, Device deviceDetails) {
        Device device = getDeviceById(id);
        device.setName(deviceDetails.getName());
        device.setDescription(deviceDetails.getDescription());
        device.setAddress(deviceDetails.getAddress());
        device.setMaxConsumption(deviceDetails.getMaxConsumption());
        device.setStatus(deviceDetails.getStatus());
        device.setUserId(deviceDetails.getUserId());
        return deviceRepository.save(device);
    }

    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }

    public Device assignDeviceToUser(Long deviceId, Long userId) {
        Device device = getDeviceById(deviceId);
        device.setUserId(userId);
        return deviceRepository.save(device);
    }
}