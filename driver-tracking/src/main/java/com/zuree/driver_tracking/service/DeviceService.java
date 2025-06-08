package com.zuree.driver_tracking.service;

import com.zuree.driver_tracking.dto.DeviceDTO;
import com.zuree.driver_tracking.dto.request.DeviceRequest;
import com.zuree.driver_tracking.model.Device;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface DeviceService {
    Optional<Device> patchDevice(Long id, DeviceDTO request);
    boolean deleteDeviceById(Long id);
    Optional<DeviceDTO> getDeviceDTOById(Long id);
    DeviceDTO toDTO(Device device);
    List<DeviceDTO> toListDTO(List<Device> devices);
    Optional<Device> registerDevice(DeviceRequest deviceRequest, Authentication authentication);
    Optional<List<DeviceDTO>> getAllDevices(Authentication authentication, int page, int size, String sortBy, String direction);
}
