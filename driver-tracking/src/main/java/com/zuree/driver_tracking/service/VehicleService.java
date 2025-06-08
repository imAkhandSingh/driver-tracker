package com.zuree.driver_tracking.service;

import com.zuree.driver_tracking.dto.VehicleDTO;
import com.zuree.driver_tracking.dto.request.VehicleRequest;
import com.zuree.driver_tracking.model.Vehicle;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface VehicleService {
    Optional<Vehicle> patchVehicle(Long id, VehicleDTO request);
    boolean deleteVehicleById(Long id);
    Optional<VehicleDTO> getVehicleDTOById(Long id);
    List<VehicleDTO> toListDTO(List<Vehicle> vehicles);
    VehicleDTO toDTO(Vehicle vehicle);
    Optional<Vehicle> registerVehicle(VehicleRequest vehicleRequest, Authentication authentication);
    Optional<List<VehicleDTO>> getAllVehicles(Authentication authentication, int page, int size, String sortBy, String direction);
}
