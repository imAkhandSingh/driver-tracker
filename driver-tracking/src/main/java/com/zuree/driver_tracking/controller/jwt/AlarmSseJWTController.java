package com.zuree.driver_tracking.controller.jwt;

import com.zuree.driver_tracking.dto.request.AlarmRequest;
import com.zuree.driver_tracking.dto.response.ErrorResponse;
import com.zuree.driver_tracking.dto.response.SuccessResponse;
import com.zuree.driver_tracking.model.Alarm;
import com.zuree.driver_tracking.service.AlarmSseEmitterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("/alarm/${api.version}")
public class AlarmSseJWTController {

    private final AlarmSseEmitterService alarmSseEmitterService;

    @Autowired
    public AlarmSseJWTController(AlarmSseEmitterService alarmSseEmitterService) {
        this.alarmSseEmitterService = alarmSseEmitterService;
    }

    @GetMapping("/stream")
    public SseEmitter streamAlarms(Authentication authentication) {
        return alarmSseEmitterService.streamAlarms(authentication);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createAlarm(@RequestBody AlarmRequest request) {
        boolean checked = alarmSseEmitterService.createAlarm(request);
        if (checked) {
            // Successful response
            return ResponseEntity.ok(new SuccessResponse<>("Alarm successfully added 🚀"));
        } else {
            // Error response
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(400, "Invalid alarm data format 🚫"));
        }
    }
}

