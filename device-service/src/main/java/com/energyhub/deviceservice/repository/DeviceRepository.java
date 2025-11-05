package com.energyhub.deviceservice.repository;

import com.energyhub.deviceservice.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByUserId(Long userId);
}