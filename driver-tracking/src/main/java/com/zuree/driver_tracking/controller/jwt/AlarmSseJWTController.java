package com.zuree.driver_tracking.controller.jwt;

import com.zuree.driver_tracking.dto.AlarmDTO;
import com.zuree.driver_tracking.dto.request.AlarmRequest;
import com.zuree.driver_tracking.dto.response.ErrorResponse;
import com.zuree.driver_tracking.dto.response.SuccessResponse;
import com.zuree.driver_tracking.service.AlarmSseEmitterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
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

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public SseEmitter streamAlarms(Authentication authentication) {
        return alarmSseEmitterService.streamAlarms(authentication);
    }

    @GetMapping("/stream/{mid}")
    public SseEmitter streamAlarmsWithoutHeader(@PathVariable Long mid) {
        return alarmSseEmitterService.streamAlarmsById(mid);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createAlarm(@RequestBody AlarmRequest request) {
        Optional<AlarmDTO> alarmDTO = alarmSseEmitterService.createAlarm(request);

        return alarmDTO.<ResponseEntity<Object>>map(dto -> ResponseEntity
                        .ok(new SuccessResponse<>(Map
                        .of("alarmId", dto.alarmId(), "msg", "Alarm successfully added 🚀"))))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(400, "Invalid alarm data format 🚫")));
    }
}

