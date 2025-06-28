package com.zuree.driver_tracking.service;

import com.zuree.driver_tracking.dto.DeviceTelemetryDTO;
import com.zuree.driver_tracking.dto.request.DeviceTelemetryRequest;
import com.zuree.driver_tracking.model.DeviceTelemetry;

import java.util.List;
import java.util.Optional;

public interface DeviceTelemetryService {
    Optional<DeviceTelemetryDTO> create(DeviceTelemetryRequest request);
    DeviceTelemetryDTO toDTO(DeviceTelemetry deviceTelemetry);
    Optional<List<DeviceTelemetryDTO>> getDeviceTelemetryDTOById(Long id, int page, int size, String sortBy, String direction);
}
