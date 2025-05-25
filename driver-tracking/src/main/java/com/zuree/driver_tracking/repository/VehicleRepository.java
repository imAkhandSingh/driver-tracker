package com.zuree.driver_tracking.repository;

import com.zuree.driver_tracking.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByVehicleId(Long vehicleId);
}
