package com.zuree.driver_tracking.controller;

import com.zuree.driver_tracking.dto.DeviceDTO;
import com.zuree.driver_tracking.dto.request.DeviceRequest;
import com.zuree.driver_tracking.dto.response.BaseResponse;
import com.zuree.driver_tracking.dto.response.ErrorResponse;
import com.zuree.driver_tracking.dto.response.SuccessResponse;
import com.zuree.driver_tracking.model.Device;
import com.zuree.driver_tracking.service.DeviceService;
import com.zuree.driver_tracking.util.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/device/${api.version}")
public class DeviceController {

    final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getDevice(@PathVariable Long id) {
        Optional<DeviceDTO> deviceDTO = deviceService.getDeviceDTOById(id);
        if (deviceDTO.isPresent()){
            return ResponseEntity.ok(new SuccessResponse<>(deviceDTO));
        } else {
            // Error response
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Device Not Found 🚫"));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getDevice(Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "deviceId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Optional<List<DeviceDTO>> deviceDTO = deviceService.getAllDevices(authentication, page, size, sortBy, direction);
        if (deviceDTO.isPresent()){
            return ResponseEntity.ok(new SuccessResponse<>(deviceDTO));
        } else {
            // Error response
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(AppConstants.CODE_NOT_FOUND, AppConstants.DEVICE_NOT_FOUND));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> registerDevice(@RequestBody DeviceRequest request, Authentication authentication) {
        try {
            Optional<Device> registeredDevice = deviceService.registerDevice(request,authentication);
            if (registeredDevice.isPresent()){
                    return ResponseEntity.ok(new SuccessResponse<>("Device register successfully 🚀"));
                } else {
                // Error response
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(404, "Manager or Vehicle not found with device id 🚫"));
                }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(401, "Error while inserting device: " + e.getMessage()));
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Object> patchDevice(@PathVariable Long id, @RequestBody DeviceDTO request) {
        Optional<Device> deviceOps = deviceService.patchDevice(id, request);
        // Error response
        return deviceOps.<ResponseEntity<Object>>map(device -> ResponseEntity.ok(new SuccessResponse<>(deviceService.toDTO(device)))).orElseGet(() -> ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(AppConstants.CODE_NOT_FOUND, AppConstants.DEVICE_NOT_FOUND)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteDevice(@PathVariable Long id) {
        try {
            boolean deleted = deviceService.deleteDeviceById(id);
            if (deleted) {
                return ResponseEntity.ok(new SuccessResponse<>( "Device deleted successfully 🚀"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(404, "Device with ID " + id + " not found 🚫"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(401, "Error deleting device: " + e.getMessage()));
        }
    }

}
