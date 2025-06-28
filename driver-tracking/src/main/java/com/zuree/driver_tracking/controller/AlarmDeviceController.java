package com.zuree.driver_tracking.controller;

import com.zuree.driver_tracking.dto.AlarmDTO;
import com.zuree.driver_tracking.dto.response.ErrorResponse;
import com.zuree.driver_tracking.dto.response.SuccessResponse;
import com.zuree.driver_tracking.service.AlarmDeviceService;
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
@RequestMapping("/alarm/${api.version}")
public class AlarmDeviceController {

    private final AlarmDeviceService alarmDeviceService;

    public AlarmDeviceController(AlarmDeviceService alarmDeviceService) {
        this.alarmDeviceService = alarmDeviceService;
    }

    @GetMapping("/device/{id}")
    public ResponseEntity<Object> getAllDeviceAlarms(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "alarmId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Optional<List<AlarmDTO>> alarmDTO = alarmDeviceService.getAllAlarms(id, page, size, sortBy, direction);
        if (alarmDTO.isPresent()){
            return ResponseEntity.ok(new SuccessResponse<>(alarmDTO));
        } else {
            // Error response
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(AppConstants.CODE_NOT_FOUND, AppConstants.DEVICE_NOT_FOUND));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAlarmById(@PathVariable Long id) {
        return ResponseEntity.ok(new SuccessResponse<>(alarmDeviceService.getAlarmById(id)));
    }

    @GetMapping("/manager/all")
    public ResponseEntity<Object> getAllManagerDeviceAlarms(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "alarmId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Optional<List<AlarmDTO>> alarmDTO = alarmDeviceService.getAllManagerAlarms(authentication, page, size, sortBy, direction);
        if (alarmDTO.isPresent()){
            return ResponseEntity.ok(new SuccessResponse<>(alarmDTO));
        } else {
            // Error response
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(AppConstants.CODE_NOT_FOUND, AppConstants.ALARM_NOT_FOUND));
        }
    }
}
