package com.zuree.driver_tracking.service.impl;

import com.zuree.driver_tracking.dto.DeviceTelemetryDTO;
import com.zuree.driver_tracking.dto.request.DeviceTelemetryRequest;
import com.zuree.driver_tracking.model.Device;
import com.zuree.driver_tracking.model.DeviceTelemetry;
import com.zuree.driver_tracking.repository.DeviceRepository;
import com.zuree.driver_tracking.repository.DeviceTelemetryRepository;
import com.zuree.driver_tracking.service.DeviceTelemetryService;
import com.zuree.driver_tracking.util.AppConstants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceTelemetryServiceImpl implements DeviceTelemetryService {

    final DeviceRepository deviceRepository;
    final DeviceTelemetryRepository deviceTelemetryRepository;

    public DeviceTelemetryServiceImpl(DeviceRepository deviceRepository, DeviceTelemetryRepository deviceTelemetryRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceTelemetryRepository = deviceTelemetryRepository;
    }


    @Override
    public DeviceTelemetryDTO toDTO(DeviceTelemetry deviceTelemetry) {
        String formattedDateTime = deviceTelemetry.getLogTime().format(AppConstants.CUSTOM_FORMATTER);
        return new DeviceTelemetryDTO(
                deviceTelemetry.getTelemetryId(),
                deviceTelemetry.getDevice().getDeviceId(),
                deviceTelemetry.getSpeed(),
                deviceTelemetry.getAcceleration(),
                deviceTelemetry.getLatitude(), deviceTelemetry.getLongitude(),
                deviceTelemetry.getDrowsiness(),
                deviceTelemetry.getRashDriving(), deviceTelemetry.getCollision(),
                formattedDateTime
        );
    }

    @Override
    @Transactional
    public Optional<List<DeviceTelemetryDTO>> getDeviceTelemetryDTOById(Long id, int page, int size, String sortBy, String direction) {
        Device device = new Device();
        device.setDeviceId(id);

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        List<DeviceTelemetry> deviceTelemetryOps = deviceTelemetryRepository.findByDevice(device, pageable);
        return Optional.of(toListDTO(deviceTelemetryOps));
    }

    private List<DeviceTelemetryDTO> toListDTO(List<DeviceTelemetry> vehicles) {
        return vehicles.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public Optional<DeviceTelemetryDTO> create(DeviceTelemetryRequest request) {
        try {
//            Device device = deviceRepository.findByDeviceId(request.deviceId());
            Device device = new Device();
            device.setDeviceId(request.deviceId());
            DeviceTelemetry deviceTelemetry = DeviceTelemetry.builder()
                    .speed(request.speed())
                    .acceleration(request.acceleration())
                    .latitude(request.latitude())
                    .longitude(request.longitude())
                    .drowsiness(request.drowsiness())
                    .rashDriving(request.rashDriving())
                    .collision(request.collision())
                    .logTime(LocalDateTime.now(ZoneId.of(AppConstants.ZONE_ID)))
                    .device(device)
                    .status("show")
                    .build();

            // ✅ Always save deviceTelemetry to DB
            deviceTelemetryRepository.save(deviceTelemetry);

            // ✅ Create DTO
            return Optional.of(toDTO(deviceTelemetry));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}

