package com.zuree.driver_tracking.service.impl;

import com.zuree.driver_tracking.dto.VehicleDTO;
import com.zuree.driver_tracking.dto.request.VehicleRequest;
import com.zuree.driver_tracking.model.Manager;
import com.zuree.driver_tracking.model.Vehicle;
import com.zuree.driver_tracking.repository.DeviceRepository;
import com.zuree.driver_tracking.repository.ManagerRepository;
import com.zuree.driver_tracking.repository.VehicleRepository;
import com.zuree.driver_tracking.service.VehicleService;
import com.zuree.driver_tracking.util.VehicleUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {

    final DeviceRepository deviceRepository;
    final ManagerRepository managerRepository;
    final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(DeviceRepository deviceRepository, ManagerRepository managerRepository, VehicleRepository vehicleRepository) {
        this.deviceRepository = deviceRepository;
        this.managerRepository = managerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Optional<Vehicle> patchVehicle(Long id, VehicleDTO request) {
        VehicleUtil vehicleUtil = new VehicleUtil();
        return vehicleRepository.findById(id).map(vehicle -> {
            vehicleUtil.patch(vehicle, request);// Maps only non-null by default
            return vehicleRepository.save(vehicle);
        });
    }

    public boolean deleteVehicleById(Long id) {
        if (vehicleRepository.existsById(id)) {
            vehicleRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<VehicleDTO> getVehicleDTOById(Long id) {
        Optional<Vehicle> vehicleOps = vehicleRepository.findById(id);
        return vehicleOps.map(this::toDTO).or(() -> Optional.ofNullable(VehicleDTO.builder().build()));
    }

    @Override
    public VehicleDTO toDTO(Vehicle vehicle) {
        return VehicleDTO.builder()
                .vehicleId(vehicle.getVehicleId())
                .vehicleNumber(vehicle.getVehicleNumber())
                .vehicleType(vehicle.getVehicleType())
                .manufacturer(vehicle.getManufacturer())
                .model(vehicle.getModel())
                .build();
    }

    @Override
    public List<VehicleDTO> toListDTO(List<Vehicle> vehicles) {
        return vehicles.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public Optional<Vehicle> registerVehicle(VehicleRequest request, Authentication authentication) {
        String username = authentication.getName();
        Optional<Manager> manager = managerRepository.findByEmail(username);
        if (manager.isPresent()) {
            Vehicle vehicle = new Vehicle();
            vehicle.setVehicleNumber(request.vehicleNumber());
            vehicle.setVehicleType(request.vehicleType());
            vehicle.setModel(request.model());
            vehicle.setManufacturer(request.manufacturer());
            vehicle.setManager(manager.get());
            return Optional.of(vehicleRepository.save(vehicle));
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<VehicleDTO>> getAllVehicles(Authentication authentication, int page, int size, String sortBy, String direction) {
        String username = authentication.getName();
        Optional<Manager> manager = managerRepository.findByEmail(username);
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        List<Vehicle> vehicleList = vehicleRepository.findByManager(manager.get(), pageable);
//        return manager.map(value -> toListDTO(value.getVehicles()));
        return Optional.of(toListDTO(vehicleList));
    }
}

