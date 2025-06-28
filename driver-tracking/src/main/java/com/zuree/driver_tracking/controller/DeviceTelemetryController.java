package com.zuree.driver_tracking.controller;

import com.zuree.driver_tracking.dto.DeviceTelemetryDTO;
import com.zuree.driver_tracking.dto.request.DeviceTelemetryRequest;
import com.zuree.driver_tracking.dto.response.SuccessResponse;
import com.zuree.driver_tracking.service.DeviceTelemetryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("/device/${api.version}/data")
public class DeviceTelemetryController {

    private final DeviceTelemetryService deviceTelemetryService;

    @Autowired
    public DeviceTelemetryController(DeviceTelemetryService deviceTelemetryService) {
        this.deviceTelemetryService = deviceTelemetryService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createDeviceTelemetry(@RequestBody DeviceTelemetryRequest request) {
        Optional<DeviceTelemetryDTO> deviceTelemetryDTO = deviceTelemetryService.create(request);
        // Successful response
        return ResponseEntity.ok(new SuccessResponse<>("Device data successfully added 🚀"));
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getDeviceTelemetry(@PathVariable Long id,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size,
                                                     @RequestParam(defaultValue = "telemetryId") String sortBy,
                                                     @RequestParam(defaultValue = "asc") String direction) {
        Optional<List<DeviceTelemetryDTO>> deviceTelemetryDTO = deviceTelemetryService.getDeviceTelemetryDTOById(id, page, size, sortBy, direction);
        // Successful response
        return ResponseEntity.ok(new SuccessResponse<>(deviceTelemetryDTO));
    }
}

