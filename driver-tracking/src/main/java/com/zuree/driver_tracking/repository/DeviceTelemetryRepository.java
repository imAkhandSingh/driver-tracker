package com.zuree.driver_tracking.repository;

import com.zuree.driver_tracking.model.Device;
import com.zuree.driver_tracking.model.DeviceTelemetry;
import com.zuree.driver_tracking.model.Manager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceTelemetryRepository extends JpaRepository<DeviceTelemetry, Long> {
    List<DeviceTelemetry> findByDevice(Device device, Pageable pageable);
}
