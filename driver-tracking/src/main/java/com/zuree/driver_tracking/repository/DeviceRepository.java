package com.zuree.driver_tracking.repository;

import com.zuree.driver_tracking.model.Device;
import com.zuree.driver_tracking.model.Manager;
import com.zuree.driver_tracking.model.Vehicle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Device findByDeviceId(Long vehicleId);
    List<Device> findByManager(Manager id, Pageable pageable);
}
