package com.zuree.driver_tracking.service.impl;

import com.zuree.driver_tracking.dto.DeviceDTO;
import com.zuree.driver_tracking.dto.request.DeviceRequest;
import com.zuree.driver_tracking.model.Device;
import com.zuree.driver_tracking.model.Manager;
import com.zuree.driver_tracking.model.Vehicle;
import com.zuree.driver_tracking.repository.DeviceRepository;
import com.zuree.driver_tracking.repository.ManagerRepository;
import com.zuree.driver_tracking.repository.VehicleRepository;
import com.zuree.driver_tracking.service.DeviceService;
import com.zuree.driver_tracking.util.AppConstants;
import com.zuree.driver_tracking.util.DeviceUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {

    final DeviceRepository deviceRepository;
    final ManagerRepository managerRepository;
    final VehicleRepository vehicleRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository, ManagerRepository managerRepository, VehicleRepository vehicleRepository) {
        this.deviceRepository = deviceRepository;
        this.managerRepository = managerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Optional<Device> patchDevice(Long id, DeviceDTO request) {
        DeviceUtil deviceUtil = new DeviceUtil();
        return deviceRepository.findById(id).map(device -> {
            deviceUtil.patch(device, request);// Maps only non-null by default
            return deviceRepository.save(device);
        });
    }

    public boolean deleteDeviceById(Long id) {
        if (deviceRepository.existsById(id)) {
            deviceRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Optional<DeviceDTO> getDeviceDTOById(Long id) {
        Optional<Device> deviceOps = deviceRepository.findById(id);
        return deviceOps.map(this::toDTO).or(() -> Optional.ofNullable(DeviceDTO.builder().build()));
    }

    @Override
    public DeviceDTO toDTO(Device device) {
        String formattedDateTime = device.getInstalledAt().format(AppConstants.CUSTOM_FORMATTER);
        return DeviceDTO.builder()
                .deviceId(device.getDeviceId())
                .deviceName(device.getDeviceName())
                .deviceType(device.getDeviceType())
                .status(device.getStatus())
                .installedAt(formattedDateTime)
                .vehicleId(device.getVehicle().getVehicleId())
                .build();
    }

    @Override
    public List<DeviceDTO> toListDTO(List<Device> devices) {
        return devices.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public Optional<Device> registerDevice(DeviceRequest request, Authentication authentication) {
        String username = authentication.getName();
        Optional<Manager> manager = managerRepository.findByEmail(username);
        Optional<Vehicle> vehicleOps = vehicleRepository.findById(request.vehicleId());
        if (manager.isPresent() && vehicleOps.isPresent()) {
            Device device = new Device();
            device.setDeviceName(request.deviceName());
            device.setDeviceType(request.deviceType());
            device.setStatus(request.status());
            device.setInstalledAt(LocalDateTime.now(ZoneId.of(AppConstants.ZONE_ID)));
            device.setManager(manager.get());
            Vehicle vehicle = vehicleOps.get();
            device.setVehicle(vehicle);
            return Optional.of(deviceRepository.save(device));
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<DeviceDTO>> getAllDevices(Authentication authentication,  int page, int size, String sortBy, String direction) {
        String username = authentication.getName();
        Optional<Manager> manager = managerRepository.findByEmail(username);
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        List<Device> deviceList = deviceRepository.findByManager(manager.get(), pageable);
        return Optional.of(toListDTO(deviceList));

//        return manager.map(value -> toListDTO(value.getDevices()));
    }
}

